package tests;

import rts.Game;
import rts.units.UnitTypeTable;

/**
 * Full headless AI-vs-AI episode, using the real microRTS Game loop.
 * No GUI, no network, no third-party game ROMs.
 */
public final class P3HeadlessMatch extends Game {
    private static final int MAX_CYCLES = 1500;
    private static final String MAP = "maps/8x8/basesWorkers8x8.xml";

    public P3HeadlessMatch() throws Exception {
        super(new UnitTypeTable(1, 1), MAP, true, false, MAX_CYCLES, 0,
              "ai.abstraction.WorkerRush", "ai.abstraction.LightRush");
    }

    private void verify() {
        final int cycles = gs.getTime();
        final boolean gameOver = gs.gameover();
        final int winner = gs.winner();

        if (cycles <= 0 || cycles > MAX_CYCLES) {
            throw new AssertionError("Invalid cycle count: " + cycles);
        }
        if (!gameOver && cycles != MAX_CYCLES) {
            throw new AssertionError("Match stopped without terminal state or cycle cap");
        }
        if (winner < -1 || winner > 1) {
            throw new AssertionError("Invalid winner: " + winner);
        }
        if (gs.getPhysicalGameState().getWidth() != 8 ||
            gs.getPhysicalGameState().getHeight() != 8) {
            throw new AssertionError("Unexpected map dimensions");
        }
        System.out.println("P3_MICRORTS_RESULT " +
            "map=" + MAP + " ai1=WorkerRush ai2=LightRush" +
            " cycles=" + cycles + " terminal=" + gameOver +
            " winner=" + winner + " maxCycles=" + MAX_CYCLES);
    }

    public static void main(String[] args) throws Exception {
        P3HeadlessMatch match = new P3HeadlessMatch();
        match.start();
        match.verify();
    }
}
