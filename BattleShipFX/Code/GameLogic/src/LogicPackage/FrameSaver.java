package LogicPackage;

public class FrameSaver {
    private int m_TurnNumber;
    private int m_PlayerNumber;
    private int m_PlayerScore;
    private char [][] m_BattleshipsBoard;
    private char [][] m_AttackBoard;
    private int m_Hits;
    private int m_Miss;
    private String m_AverageMoveTime;
    private int m_OpponentScore;
    private int m_BattleShipLeftToTheOpponent;
    private int m_NumberOfMine;
    private String OpponentShipLeft;
    private String PlayerhipDiteles;

    public void InitFrame(Player player, int playerNumber, int turnNumber, String avgTime, int opponentScore,String i_PlayerhipDiteles,String i_OpponentShipLeft)
    {
        m_BattleshipsBoard = new char [player.getM_charPlayerBoard().length][player.getM_charPlayerBoard().length];
        m_AttackBoard = new char [player.getM_charPlayerBoard().length][player.getM_charPlayerBoard().length];
        m_TurnNumber = turnNumber;
        m_PlayerNumber =  playerNumber;
        m_PlayerScore = player.getM_PlayerScore();
        copyArray(m_BattleshipsBoard,player.getM_charPlayerBoard());
        copyArray(m_AttackBoard,player.getM_AttackBoard());
        m_Hits = player.getNumberOfHits();
        m_Miss = player.getM_NumberOfTunes() - m_Hits;
        m_AverageMoveTime = avgTime;
        m_OpponentScore = opponentScore;
        m_NumberOfMine = player.getM_NumberOfMinesLeft();
        OpponentShipLeft = i_OpponentShipLeft;
        PlayerhipDiteles = i_PlayerhipDiteles;

    }

    public String getOpponentShipLeft() {
        return OpponentShipLeft;
    }

    public String getPlayerhipDiteles() {
        return PlayerhipDiteles;
    }

    public int Getm_TurnNumber() {
        return m_TurnNumber;
    }

    public int Getm_PlayerNumber() {
        return m_PlayerNumber;
    }

    public int Getm_m_PlayerScore() {
        return m_PlayerScore;
    }

    public int Getm_Hits() {
        return m_Hits;
    }

    public int Getm_Miss() {
        return m_Miss;
    }

    public String Getm_AverageMoveTime() {
        return m_AverageMoveTime;
    }

    public int Getm_OpponentScore() {
        return m_OpponentScore;
    }

    public int Getm_BattleShipLeftToTheOpponent() {
        return m_BattleShipLeftToTheOpponent;
    }

    public int Getm_NumberOfMinet() {
        return m_NumberOfMine;
    }

    public char [][] Getm_BattleshipsBoard() {
        return m_BattleshipsBoard;
    }
    public char [][] Getm_AttackBoard() {
        return m_AttackBoard;
    }
    private void copyArray(char [][] src, char [][] des)
    {
        for(int i=0;i<src.length;i++)
            for(int j=0;j<src.length;j++)
                src[i][j] = des[i][j];
    }
}
