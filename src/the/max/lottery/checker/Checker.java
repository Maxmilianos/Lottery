package the.max.lottery.checker;

import java.util.HashMap;
import java.util.Random;

public class Checker {
	private Random random;
    private int rspeed;
    private int sb;
    private int loops;
    private int maxloops;
    private HashMap<Integer, Integer> speeds;
    
    public Checker() {
        random = new Random();
        rspeed = 1;
        sb = 0;
        loops = 0;
        maxloops = 100;
        speeds = new HashMap<Integer, Integer>();
        speeds.put(0, 1);
        final double n = 1.3;
        final double n2 = 12.3;
        maxloops *= (int)n;
        for (int i = 1; i <= 40; ++i) {
            int n3 = (int)(i + random.nextInt(i) / 1.5);
            final int n4 = (int)(n3 * n);
            if (i == 20) {
                n3 *= (int)0.85;
            } else if (i == 30) {
                n3 *= (int)0.65;
            }  else if (i == 40) {
                n3 *= (int)0.45;
            }
            speeds.put((int)(n3 * n2), n4);
        }
    }
    
    public boolean should() {
        loops += 1;
        sb += 1;
        if (speeds.get(loops) != null) {
            rspeed = speeds.get(loops);
        }
        if (sb >= rspeed) {
            sb = 0;
            return true;
        }
        return false;
    }
    
    public void check() {
        if (loops >= maxloops) {
            rspeed += 2;
        }
    }
}
