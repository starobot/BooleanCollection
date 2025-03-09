package bot.staro;

import bot.staro.booleans.RemoteProcessBoolean;
import bot.staro.booleans.RogueBoolean;
import bot.staro.booleans.SegfaultBoolean;

public class Main {
    public static void main(String[] args) {
        RogueBoolean rogueBoolean = new RogueBoolean();
        rogueBoolean.setValue(true);
        SegfaultBoolean seg = new SegfaultBoolean(false);
        seg.setValue(true);
        System.out.println(rogueBoolean);

        RemoteProcessBoolean remoteProcessBoolean = new RemoteProcessBoolean(true);
        System.out.println(remoteProcessBoolean.getValue());
        remoteProcessBoolean.setValue(false);
        System.out.println(remoteProcessBoolean.getValue());
    }
}