package LogicPackage;

import java.util.ArrayList;
import java.util.List;

public class DateSaver {
        private List<FrameSaver> allFrames;
        private int index=-1;

        public DateSaver ()
        {
            allFrames = new ArrayList<>();
        }
        public void NewFrame(Player player,int playerNumber, int turnNumber, String avgTime,int opponentScore,String i_PlayerhipDiteles,String i_OpponentShipLeft)
        {
            FrameSaver frame = new FrameSaver();
            frame.InitFrame(player, playerNumber,  turnNumber,  avgTime, opponentScore,i_PlayerhipDiteles,i_OpponentShipLeft);
            allFrames.add(frame);
            index++;
        }
        public FrameSaver GetNextFrame()
        {
            index++;
            return allFrames.get(index);
        }
        public FrameSaver GetPrevFrame()
        {
            index--;
            return allFrames.get(index);
        }
        public boolean StillHavePrev()
        {
            return index != 0;
        }
        public boolean StillHaveNext()
        {
            return index != (allFrames.size() -1);
        }
}
