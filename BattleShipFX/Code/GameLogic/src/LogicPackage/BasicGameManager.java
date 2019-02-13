package LogicPackage;

import BattleShipInput.*;
import javafx.animation.Animation;

import java.util.Date;
import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

public class BasicGameManager {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "BattleShipInput";
    private BattleShipGame game;
    private Player [] m_Players = new Player[2];
    private Date m_TimeOfStartGame;
    private boolean ShipSinked = false;
    private boolean m_HitMine = false;

    public BattleShipGame getGame() {
        return game;
    }

    public boolean getHitMine() {
        return m_HitMine;
    }

    public Player GetPlayer(int i_PlayerNumber)
    {
        return m_Players[i_PlayerNumber];
    }
    public void LoadGame(String i_Input) throws Exception {
        LoadFile(i_Input); //"C:\\Users\\David\\IdeaProjects\\BattleShip\\GameLogic\\src\\resources\\battleShip_5_basic.xml"
        CheckXML();
        setPlayer();
    }
    public boolean getShipSinked()
    {
        return ShipSinked;
    }

    public void setShipSinked(boolean shipSinked) {
        ShipSinked = shipSinked;
    }

    private void CheckXML() throws Exception  {
        CheckBoardSize();
        CheckGameType();
        CheckShipsCategory();
        //CheckShipsSize();
        CheckShipsAmount();
        //checkTypeShipSize();
        CheckShipsId();
        CheckNumberOfMine();
    }

    private void checkTypeShipSize() throws Exception {
            for (BattleShipGame.ShipTypes.ShipType shipType : game.getShipTypes().getShipType())
            {
                if(shipType.getAmount() > 1 && shipType.getLength() == 4)
                    throw new Exception("Invalid file. Allow up to one ship size 4 ");
                if(shipType.getAmount() > 2 && shipType.getLength() == 3)
                    throw new Exception("Invalid file. Allow up to two ship size 3");
                if(shipType.getAmount() > 3 && shipType.getLength() == 2)
                    throw new Exception("Invalid file. Allow up to three ship size 2 ");
                if(shipType.getAmount() > 4 && shipType.getLength() == 1)
                    throw new Exception("Invalid file. Allow up to four ship size 1 ");
            }
    }

    private void CheckNumberOfMine() throws Exception{
        if(game.getMine()!= null) {
            if (game.getMine().getAmount() < 0) {
                throw new Exception("Invalid file. The number of mines have to be at least zero");
            }
        }
    }

    private void CheckShipsId()throws Exception  {
        int amount = 0;
        for(BattleShipGame.Boards.Board board : game.getBoards().getBoard()) {
                for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                    for (BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                        if (shipTypes.getId().compareTo((ship.getShipTypeId())) == 0) {
                            amount++;
                        }
                    }
                    if(amount == 0)
                        throw new Exception("Invalid file. There is a ship id that no defined in the game type");
                    amount = 0;
            }
        }
    }

    private void CheckShipsAmount() throws Exception {
        int amount = 0;
        for(BattleShipGame.Boards.Board board : game.getBoards().getBoard()) {
                for (BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                    for (BattleShipGame.Boards.Board.Ship ship : board.getShip()) {
                        if (shipTypes.getId().compareTo((ship.getShipTypeId())) == 0) {
                            amount++;
                        }
                }
                    if(shipTypes.getAmount() != amount)
                        throw new Exception("Invalid file. The amount in the ship type is different from the amount of ships ");
                    amount = 0;
            }
        }
    }

    public void LoadFile(String i_address) throws Exception {

            File file = new File(i_address);
            try {
                if(!file.exists())
                    throw new Exception("File is not exists");
                if(!ContainsXml(i_address))
                    throw new Exception("File is not finish with '.xml' ending");
                game = deserializeFromFile(file);
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
    public boolean ContainsXml(String i_address)
    {
        String check = i_address.toUpperCase();

        if( check.contains(".XML") && check.lastIndexOf(".XML") == check.length() - 4)
        {
            return  true;
        }
        return false;
    }

    private static BattleShipGame deserializeFromFile (File in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(BattleShipGame.class);
        Unmarshaller u = jc.createUnmarshaller();
        return (BattleShipGame) u.unmarshal(in);
    }

    private void setPlayer() throws Exception
    {
        int i=0;
        for(BattleShipGame.Boards.Board board : game.getBoards().getBoard())
        {
            m_Players[i] = new Player();
            m_Players[i].setGame(game);
            m_Players[i].SetSizeBoard(game.getBoardSize());
            m_Players[i].setM_ListOfBattleShips(board.getShip());
            m_Players[i].setM_NumberOfMinesLeft(game.getMine().getAmount());
            i++;
        }
    }

    private void CheckBoardSize() throws Exception {
        if(game.getBoardSize()<5 || (game.getBoardSize()>20) )
        {
            throw new Exception("Invalid file. The board must to be between 5<board size <20");
        }
    }

    private void CheckGameType()throws Exception
    {
        if(!(game.getGameType().contentEquals("BASIC") || (game.getGameType().contentEquals("ADVANCE"))))
            throw new Exception("Invalid file. The game have to 'BASIC'. please check in the xml");
    }

    private void CheckShipsCategory()throws Exception
    {
        for (BattleShipGame.ShipTypes.ShipType shipType : game.getShipTypes().getShipType())
        {
            if(!CheckOneShipCategory(shipType.getCategory()))
                throw new Exception("Invalid file. The ship category have to much the ship category in the ship type");
        }
    }

    private boolean CheckOneShipCategory(String i_shipType)
    {
        if((i_shipType.contentEquals("REGULAR")) || i_shipType.contentEquals("L_SHAPE"))
            return true;
        return false;
    }
    private void CheckShipsSize()throws Exception {
        for (BattleShipGame.ShipTypes.ShipType shipType : game.getShipTypes().getShipType()) {
            if (!CheckOneShipCSize(shipType.getLength()))
                throw new Exception("Invalid file. All the ship size have to be between 1 to 4 ");
        }
    }

    private boolean CheckOneShipCSize(int i_length)
    {
        if((i_length >= 1) && (i_length <= 4))
            return true;
        return false;
    }


    public boolean UpdateBoards(int m_playerNumber, int x, int y) {
        boolean isHit;
        m_Players[m_playerNumber].AddTurn();
        isHit = UpdatePlayerBoard(m_playerNumber, x, y);
        UpdateRivalBoard(m_playerNumber, x, y);
        m_Players[m_playerNumber].EndTurn();
        return isHit;
    }

    private boolean UpdatePlayerBoard(int m_playerNumber, int x, int y) {
        m_HitMine =false;
        int score;
        boolean Hit =m_Players[(m_playerNumber + 1) % 2].HaveShip(x,y);
        boolean HitMine =m_Players[(m_playerNumber + 1) % 2].HaveMine(x,y);
        if(HitMine)
        {
            m_Players[m_playerNumber].AddHit();
            m_Players[m_playerNumber].SetHits(x,y , 'H');
            UpdatePlayerBoard(((m_playerNumber + 1) % 2),x,y);
            score= m_Players[m_playerNumber].SetAttacked(x, y);
            if (score != 0) {
                m_Players[((m_playerNumber + 1) % 2)].AddScore(score);

            }

        }
        else if(Hit) {
            m_Players[m_playerNumber].SetHits(x,y , 'H');
        }
        else
        {
            m_Players[m_playerNumber].SetHits(x,y , 'M');
        }
        m_HitMine = HitMine;
        return Hit;
    }

    private void UpdateRivalBoard(int m_playerNumber, int x, int y) {
        int score;
        if(m_Players[(m_playerNumber + 1) % 2].HaveShip(x,y)) {
            m_Players[m_playerNumber].AddHit();
        }
        score= m_Players[(m_playerNumber + 1) % 2].SetAttacked(x, y);
        if (score != 0) {
            m_Players[m_playerNumber].AddScore(score);

        }
    }

    public boolean AlreadyAttackThisPlace(int i_playerNumber, int x, int y)
    {
        return m_Players[i_playerNumber].AlreadyAttack(x,y);
    }

    public void SetDate() {
        m_TimeOfStartGame = new Date();
    }

    public String GetTimeFormBeginningGame()
    {
        long returnValueLong;
        String returnValue;
        Date currentTime = new Date();
        returnValueLong = (currentTime.getTime() - m_TimeOfStartGame.getTime());
        returnValue= String.valueOf((returnValueLong/(1000 * 60)) % 60) + ":" + String.valueOf((returnValueLong/1000) % 60);
        return returnValue;
    }

    public int NumberOfTurnTotal()
    {
        return m_Players[0].getM_NumberOfTunes() + m_Players[1].getM_NumberOfTunes();
    }

    public int NumberOfTurnOfPlayer(int i_numberOfPlayer)
    {
        return m_Players[i_numberOfPlayer].getM_NumberOfTunes();
    }

    public int GetPlayerHit(int i_numberOfPlayer) {return m_Players[i_numberOfPlayer].getNumberOfHits();}
    public int GetPlayerMiss(int i_numberOfPlayer) {return m_Players[i_numberOfPlayer].getM_NumberOfTunes() - m_Players[i_numberOfPlayer].getNumberOfHits();}

    public void StartTurnOfPlayer(int i_playerNumber) {
        m_Players[i_playerNumber].StartTurn();
    }

    public String AverageTimePlayerTurn(int i_numberOfPlayer)
    {
        long avgTime;
        String returnValue = "0";
        if( m_Players[i_numberOfPlayer].getM_NumberOfTunes() != 0 ) {
            avgTime = m_Players[i_numberOfPlayer].getM_TotalTimePlayerHasPlayed() / m_Players[i_numberOfPlayer].getM_NumberOfTunes();
            returnValue = String.valueOf((avgTime / (1000 * 60)) % 60) + ":" + String.valueOf((avgTime / 1000) % 60);
        }
        return returnValue;
    }

    public void DecreaseTurn(int m_playerNumber) {
        m_Players[m_playerNumber].DecreaseTurn();
    }

    public boolean CheckIfPlayerWon(int m_playerNumber) {
        return !m_Players[(m_playerNumber + 1) % 2].CheckIfHaveShips();
    }

    public boolean OutOfRange(int x) {
        if(x < 0 || x >= game.getBoardSize())
            return true;
        return false;
    }

    public int PlayerNumberOfShip(int i_PlayerNumber)
    {
        return m_Players[i_PlayerNumber].NumberOfShipLeft();
    }

    public void addMine(int m_playerNumber, int x, int y)
    {
        m_Players[m_playerNumber].addMine( x, y);
    }
    public boolean PossiblePlaceForMine(int player,int x,int y)
    {
        return m_Players[player].PossiblePlaceForMine(x,y);
    }

    public String getPlayerhipDiteles(int playerNumber) {
        return m_Players[playerNumber].getAllShipDiteles();
    }
}


