package utils;

import java.security.PublicKey;

public class Constants {

    public static  class Direction{
        public static final int UP = 0;
        public static final int RIGHT = 1;
        public static final int LEFT = 2;
        public static final int DOWN = 3;
    }

    public static class PLayerConstants{
        public static final int ATTACK_1 = 0;
        public static final int ATTACK_2 = 1;
        public static final int ATTACK_3 = 2;
        public static final int CLIMB = 3;
        public static final int DEATH = 4;
        public static final int DOUBLE_JUMP = 5;
        public static final int HURT = 6;
        public static final int IDLE = 7;
        public static final int JUMP = 8;
        public static final int PUNCH = 9;
        public static final int RUN = 10;
        public static final int RUN_ATTACK = 11;

        public static int GetSpriteAmount(int player_action){
            switch (player_action){
                case ATTACK_1, CLIMB, DEATH, DOUBLE_JUMP, PUNCH, RUN, RUN_ATTACK:
                    return 6;
                case ATTACK_2, ATTACK_3:
                    return 8;
                case HURT:
                    return 2;
                case IDLE, JUMP:
                    return 4;
                default:
                    return 1;
            }
        }
    }
}
