package LogicPackage;

import BattleShipInput.*;

public class PlayerShip {
    private BattleShipGame.Boards.Board.Ship m_ship;
    private int m_physicalLength;
    private boolean m_IsSink = false;
    private int m_Score;
    private char m_Symbol;

    public PlayerShip(BattleShipGame.Boards.Board.Ship i_ship, int i_Score, int i_length, char i_Symbol)
    {
        this.m_Score = i_Score;
        this.m_ship = i_ship;
        this.m_physicalLength = i_length;
        m_Symbol = i_Symbol;
    }

    public void setM_physicalLength(int m_physicalLength) {
        this.m_physicalLength = m_physicalLength;
    }

    public char getM_Symbol() {
        return m_Symbol;
    }

    public int getM_physicalLength() {
        return m_physicalLength;
    }

    public BattleShipGame.Boards.Board.Ship getM_ship() {
        return m_ship;
    }

    public boolean isM_IsSink() {
        return m_IsSink;
    }

    public int  setAttack() {
        m_physicalLength--;
        if(m_physicalLength == 0)
        {
            m_IsSink = true;
            return m_Score;

        }
        return 0;
    }
}
