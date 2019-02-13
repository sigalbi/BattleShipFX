package LogicPackage;

import BattleShipInput.*;
import com.sun.javafx.binding.StringFormatter;

import java.util.Date;
import java.util.*;
import java.util.List;

public class Player {
    private PlayerShip [][] m_PlayerBoard ;
    private char [][] m_charPlayerBoard;
    private char [][] m_AttackBoard;
    private ArrayList<PlayerShip> m_ListOfBattleShips = new ArrayList<PlayerShip>();
    private int m_PlayerScore;
    private BattleShipGame game;
    private Date m_TimeOfStartTurn;
    private long m_TotalTimePlayerHasPlayed = 0;
    private int m_NumberOfTurns = 0;
    private int m_NumberOfHits = 0;
    private int m_NumberOfMinesLeft;

    public int getM_NumberOfMinesLeft() {
        return m_NumberOfMinesLeft;
    }
    public void setM_NumberOfMinesLeft(int i_mine)
    {
        m_NumberOfMinesLeft = i_mine;
    }

    public int getNumberOfHits() {
        return m_NumberOfHits;
    }

    public long getM_TotalTimePlayerHasPlayed() {
        return m_TotalTimePlayerHasPlayed;
    }

    public char[][] getM_charPlayerBoard() { return m_charPlayerBoard; }

    public char[][] getM_AttackBoard() {
        return m_AttackBoard;
    }

    public PlayerShip[][] getM_PlayerBoard() {
        return m_PlayerBoard;
    }

    public void setGame(BattleShipGame game) {
        this.game = game;
    }

    public int getM_NumberOfTunes() {
        return m_NumberOfTurns;
    }

    public int getM_PlayerScore() {
        return m_PlayerScore;
    }

    private void CheckBoard() throws Exception {
        for(int i=0;i<m_PlayerBoard.length ;i++){
            for(int j=0 ; j<  m_PlayerBoard.length ; j++)
            {
                for (int k = -1 ; k<=1 ; k++)
                {
                    for (int l=-1 ; l<=1 ;l++)
                    {
                        if(((k+i) >= 0) && (k+i < m_PlayerBoard.length) &&((l+j) >= 0 && l+j < m_PlayerBoard.length))
                            if((m_PlayerBoard[i+k][j+l] != null) && (m_PlayerBoard[i][j] != null) && (m_PlayerBoard[i+k][j+l] != m_PlayerBoard[i][j]))
                                throw new Exception("Invalid file. It has overlapping ships");
                    }
                }
            }
        }
    }
    public void setM_ListOfBattleShips(List<BattleShipGame.Boards.Board.Ship> i_ListOfBattleShips)throws Exception
    {
        int i=0;
        for (BattleShipGame.Boards.Board.Ship ship :i_ListOfBattleShips)
        {
            for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                if (shipTypes.getId().compareTo((ship.getShipTypeId())) == 0) {
                    if(shipTypes.getCategory().compareTo("L_SHAPE") == 0)
                        m_ListOfBattleShips.add(new PlayerShip(ship, shipTypes.getScore(),shipTypes.getLength()*2 -  1, (char)(i+'A')));
                    else
                        m_ListOfBattleShips.add(new PlayerShip(ship, shipTypes.getScore(),shipTypes.getLength(), (char)(i+'A')));

                }
                i++;
            }
            i=0;
        }
        Set_PlayerBoard();
        CheckBoard();
    }

    public void SetSizeBoard(int i_Size)
    {
        m_PlayerBoard = new PlayerShip [i_Size][i_Size];
        for(int i=0;i<i_Size;i++)
            for(int j=0;j<i_Size;j++)
                m_PlayerBoard[i][j] = null;
        m_AttackBoard = new char[i_Size][i_Size];
        for(int i=0;i<i_Size;i++)
            for(int j=0;j<i_Size;j++)
                m_AttackBoard[i][j] = ' ';
        m_charPlayerBoard = new char[i_Size][i_Size];
        for(int i=0;i<i_Size;i++)
            for(int j=0;j<i_Size;j++)
                m_charPlayerBoard[i][j] = ' ';
    }

    private void Set_PlayerBoard()throws Exception
    {
          for(PlayerShip ship :m_ListOfBattleShips )
          {
              addShipToBoard(ship);
          }
    }

    private void addShipToBoard(PlayerShip ship) throws Exception
    {
        for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
            if(shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0 ) {
                switch (ship.getM_ship().getDirection())
                {
                    case "ROW": {
                        DirectionRight(ship);
                        break;
                    }
                    case "COLUMN": {
                        DirectionDown(ship);
                        break;
                    }
                    case "RIGHT_UP": {
                        DirectionLeft(ship);
                        DirectionUp(ship);
                        break;
                    }
                    case "RIGHT_DOWN": {
                        DirectionLeft(ship);
                        DirectionDown(ship);
                        break;
                    }
                    case "UP_RIGHT": {
                        DirectionRight(ship);
                        DirectionDown(ship);
                        break;
                    }
                    case "DOWN_RIGHT": {
                        DirectionRight(ship);
                        DirectionUp(ship);
                        break;
                    }
                    default:
                    {
                        throw new Exception("Invalid file. The ship direction have to be one of the follow:\n 'ROW' ,'COLUMN' ,'RIGHT_UP' ,'RIGHT_DOWN','UP_RIGHT','DOWN_RIGHT'");
                    }
                }
            }
        }
    }

    private void DirectionUp(PlayerShip ship) throws Exception
    {
        try {
            for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                if(shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0 )
                    for (int i = 0; i < shipTypes.getLength(); i++) {
                        m_PlayerBoard[ship.getM_ship().getPosition().getY() - i - 1 ][ship.getM_ship().getPosition().getX() - 1] = ship;
                        m_charPlayerBoard[ship.getM_ship().getPosition().getY() - i - 1 ][ship.getM_ship().getPosition().getX() - 1 ] = ship.getM_Symbol();
                    }
            }
        }
        catch (ArrayIndexOutOfBoundsException ex )
        {
            throw new Exception("Invalid file. There is a ship located somewhere outside the board size");
        }

    }

    private void DirectionDown(PlayerShip ship) throws Exception
    {
        try {
            for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                if(shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0 )
                    for (int i = 0; i < shipTypes.getLength(); i++) {
                        m_PlayerBoard[ship.getM_ship().getPosition().getY() + i - 1][ship.getM_ship().getPosition().getX() - 1] = ship;
                        m_charPlayerBoard[ship.getM_ship().getPosition().getY() + i - 1][ship.getM_ship().getPosition().getX() - 1] = ship.getM_Symbol();
                    }
        }

        }
        catch (ArrayIndexOutOfBoundsException ex)
        {
            throw new Exception("Invalid file. There is a ship located somewhere outside the board size");
        }
    }

    private void DirectionLeft(PlayerShip ship)throws Exception
    {
        try {
            for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
                if(shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0 )
                    for (int i = 0; i < shipTypes.getLength(); i++) {
                        m_PlayerBoard[ship.getM_ship().getPosition().getY() - 1][ship.getM_ship().getPosition().getX() - i - 1] = ship;
                        m_charPlayerBoard[ship.getM_ship().getPosition().getY() - 1][ship.getM_ship().getPosition().getX() - i - 1 ] = ship.getM_Symbol();
                    }
            }
        }
         catch (ArrayIndexOutOfBoundsException ex)
        {
            throw new Exception("Invalid file. There is a ship located somewhere outside the board size");
        }
    }

    private void DirectionRight(PlayerShip ship)throws Exception
    {
       try {
           for(BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
               if(shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0 )
                   for (int i = 0; i < shipTypes.getLength(); i++) {
                       m_PlayerBoard[ship.getM_ship().getPosition().getY() - 1][ship.getM_ship().getPosition().getX() + i - 1 ] = ship;
                       m_charPlayerBoard[ship.getM_ship().getPosition().getY() - 1][ship.getM_ship().getPosition().getX() + i - 1 ] = ship.getM_Symbol();
                   }
           }
       }
         catch (ArrayIndexOutOfBoundsException ex)
        {
            throw new Exception("Invalid file. There is a ship located somewhere outside the board size");
        }
    }

    public void SetHits(int x, int y , char i_char) {
        m_AttackBoard[y][x] = i_char;
    }

    public int SetAttacked(int x, int y) {
        int returnScore =0;
        if (m_PlayerBoard[y][x] != null) {
            returnScore = m_PlayerBoard[y][x].setAttack();
            m_charPlayerBoard[y][x] = 'H';
            m_PlayerBoard[y][x] = null;
        }
        else if(m_charPlayerBoard[y][x] != 'H')
            m_charPlayerBoard[y][x] = 'M';

        return returnScore;
    }

    public void AddTurn()
    {
        m_NumberOfTurns++;
    }

    public void AddScore(int score) { m_PlayerScore += score;}

    public void AddHit() {m_NumberOfHits++;}

    public boolean HaveShip(int x, int y) {
         return m_PlayerBoard[y][x] != null;
    }

    public void EndTurn()
    {
        Date currentTime = new Date();
        m_TotalTimePlayerHasPlayed += (currentTime.getTime() - m_TimeOfStartTurn.getTime());
    }

    public void StartTurn()
    {
        m_TimeOfStartTurn = new Date();
    }

    public boolean AlreadyAttack(int x, int y) {
        return m_AttackBoard[y][x] != ' ';
    }

    public void DecreaseTurn() {
        m_NumberOfTurns--; }

    public boolean CheckIfHaveShips() {
        boolean returnValue = false;
        for (PlayerShip ship :m_ListOfBattleShips) {
            if (!ship.isM_IsSink()) {
                returnValue = true;
            }
        }
        return returnValue;
    }

    public int NumberOfShipLeft()
    {
        int numberOfShip=0;
        for (PlayerShip ship :m_ListOfBattleShips) {
            if (!ship.isM_IsSink()) {
                numberOfShip++;
            }
        }
        return numberOfShip;
    }

    public void addMine(int x, int y) {
        m_NumberOfMinesLeft--;
        m_charPlayerBoard[y][x] = 'I';
    }

    public boolean HaveMine(int x, int y) {
        boolean haveMine =false;
        if(m_charPlayerBoard[y][x] == 'I')
        {
            m_charPlayerBoard[y][x] = 'H';
            haveMine= true;
        }
       return haveMine;
    }
    public boolean PossiblePlaceForMine(int x,int y)
    {
        if(y == 0 ) {
            if(checkLowerLine(x,y)&&checkMiddleLine(x,y))
                return true;
        }
        else if(y == m_AttackBoard.length -1) {
            if(checkUpperLine(x, y)&&checkMiddleLine(x,y))
            return true;
        }
        else if(checkLowerLine(x,y) && checkMiddleLine(x,y) && checkUpperLine(x, y))
            return true;
        return false;
    }

    private boolean checkLowerLine(int x,int y) {

    if(x == 0) {
        if (m_PlayerBoard[y + 1][x + 1] == null && m_PlayerBoard[y + 1][x] == null)
            return true;
    }
    else if(x == m_AttackBoard.length -1) {
        if (m_PlayerBoard[y + 1][x] == null && m_PlayerBoard[y + 1][x - 1] == null)
            return true;
    }
    else if( m_PlayerBoard[y+1][x+1] == null && m_PlayerBoard[y+1][x] == null &&m_PlayerBoard[y+1][x-1] == null)
            return true;
    return false;
    }

    private boolean checkMiddleLine(int x,int y) {
        if(x == 0) {
            if (m_PlayerBoard[y][x + 1] == null)
                return true;
        }
        else if(x == m_AttackBoard.length -1) {
            if (m_PlayerBoard[y][x - 1] == null)
                return true;
        }
        else if( m_PlayerBoard[y][x+1] == null &&m_PlayerBoard[y][x-1] == null)
              return true;
        return false;

    }

    private boolean checkUpperLine(int x,int y) {
        if(x == 0) {
            if (m_PlayerBoard[y - 1][x + 1] == null && m_PlayerBoard[y - 1][x] == null)
                return true;
        }
        else if(x == m_AttackBoard.length -1) {
            if (m_PlayerBoard[y - 1][x] == null && m_PlayerBoard[y - 1][x - 1] == null)
                return true;
        }
        else if(m_PlayerBoard[y-1][x+1] == null && m_PlayerBoard[y-1][x] == null &&m_PlayerBoard[y-1][x-1] == null)
                    return true;
        return false;


    }

    public String getAllShipDiteles() {
        String str = null;
        String Result = null;
        int numberOfShip  = 0;
        for (BattleShipGame.ShipTypes.ShipType shipTypes : game.getShipTypes().getShipType()) {
            for (PlayerShip ship : m_ListOfBattleShips) {
                if (!ship.isM_IsSink() && shipTypes.getId().compareTo((ship.getM_ship().getShipTypeId())) == 0) {
                    numberOfShip++;
                }
            }
            str = shipTypes.getId() + ": " + Integer.toString(numberOfShip) + '\n';
            numberOfShip = 0 ;
            if (Result == null) {
                Result = str;
            } else {
                Result += str;
            }
        }
        return Result;

    }
}
