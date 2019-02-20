//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.io.File;
import java.io.FilenameFilter;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class GomokuReferee2 extends Frame implements Runnable {
    GomokuBoard b;
    GomokuPlayer whitePlayer;
    GomokuPlayer blackPlayer;
    Class[] players = new Class[100];
    int playerCount = 0;
    Choice whiteChoice = new Choice();
    Choice blackChoice = new Choice();
    Button startButton;
    Button quitButton;
    Panel controls;
    Label statusLabel;
    Thread playThread = null;
    boolean pause = false;
    protected static boolean batch = false;
    protected static boolean log = false;
    private static double timeLimit = 10.0D;
    protected static double pauseTime = 1.0D;
    static final int space = 80;
    static final int MAXPLAYERS = 100;

    public GomokuReferee2() {
        super("Gomoku AI Player Test Platform");

        try {
            File var1 = new File(".");
            String[] var2 = var1.list(new FilenameFilter() {
                public boolean accept(File var1, String var2) {
                    return var2.endsWith(".class");
                }
            });
            URL var3 = var1.toURL();
            URL[] var4 = new URL[]{var3};
            URLClassLoader var5 = new URLClassLoader(var4);
            String[] var6 = var2;
            int var7 = var2.length;

            for(int var8 = 0; var8 < var7; ++var8) {
                String var9 = var6[var8];

                try {
                    var9 = var9.substring(0, var9.length() - 6);
                    Class var10 = var5.loadClass(var9);
                    if (GomokuPlayer.class.isAssignableFrom(var10) && var10 != GomokuPlayer.class && (!batch || var10 != HumanPlayer.class)) {
                        int var11 = this.playerCount;

                        while(true) {
                            --var11;
                            if (var11 < 0 || this.whiteChoice.getItem(var11).compareTo(var9) <= 0) {
                                this.whiteChoice.insert(var9, var11 + 1);
                                this.blackChoice.insert(var9, var11 + 1);
                                this.players[var11 + 1] = var10;
                                ++this.playerCount;
                                break;
                            }

                            this.players[var11 + 1] = this.players[var11];
                        }
                    }
                } catch (ClassNotFoundException var12) {
                    System.err.println(var12);
                }
            }
        } catch (MalformedURLException var13) {
            System.err.println(var13);
        }

        this.b = new GomokuBoard();
        if (!batch) {
            GomokuBoard var10001 = this.b;
            GomokuBoard var10002 = this.b;
            int var19 = 8 * 80;
            var10002 = this.b;
            GomokuBoard var10003 = this.b;
            this.b.setSize(var19, 8 * 80);
            var10001 = this.b;
            var10002 = this.b;
            var19 = 8 * 80 + 10;
            var10002 = this.b;
            var10003 = this.b;
            this.setSize(var19, 8 * 80 + 80);
            this.setResizable(false);
            this.setLayout(new BorderLayout());
            this.add("North", this.b);
            Label var14 = new Label("Status:");
            Label var15 = new Label("White:");
            Label var16 = new Label("Black:");
            Label var17 = new Label("");
            Label var18 = new Label("");
            this.statusLabel = new Label("White to start       ");
            this.statusLabel.setForeground(Color.blue);
            this.startButton = new Button("New game");
            this.quitButton = new Button("Quit");
            this.controls = new Panel();
            this.controls.setLayout(new GridLayout(2, 5));
            this.controls.add(var14);
            this.controls.add(var15);
            this.controls.add(var16);
            this.controls.add(var17);
            this.controls.add(var18);
            this.controls.add(this.statusLabel);
            this.controls.add(this.whiteChoice);
            this.controls.add(this.blackChoice);
            this.controls.add(this.startButton);
            this.controls.add(this.quitButton);
            this.add("South", this.controls);
            this.setVisible(true);
        }

    }

    public void init() {
        if (!batch) {
            this.statusLabel.setText("White starts");
        }

        this.b.init();

        try {
            this.whitePlayer = (GomokuPlayer)this.players[this.whiteChoice.getSelectedIndex()].newInstance();
            this.blackPlayer = (GomokuPlayer)this.players[this.blackChoice.getSelectedIndex()].newInstance();
            this.pause = !batch && !(this.whitePlayer instanceof HumanPlayer) && !(this.blackPlayer instanceof HumanPlayer);
        } catch (Exception var2) {
            System.err.println(var2);
            System.exit(1);
        }

        this.b.repaint();
    }

    public void run() {
        ThreadMXBean var8 = ManagementFactory.getThreadMXBean();
        if (!var8.isCurrentThreadCpuTimeSupported()) {
            var8 = null;
        }

        while(this.b.getWinner() == null) {
            long var2 = var8 == null ? 0L : var8.getCurrentThreadCpuTime();
            Color var7 = this.b.getTurn();

            Move var1;
            try {
                if (var7 == Color.white) {
                    var1 = this.whitePlayer.chooseMove(this.b.getPublicBoard(), Color.white);
                } else {
                    var1 = this.blackPlayer.chooseMove(this.b.getPublicBoard(), Color.black);
                }
            } catch (Exception var12) {
                var1 = null;
            }

            long var4 = var8 == null ? 0L : var8.getCurrentThreadCpuTime();
            double var9 = (double)(var4 - var2) / 1.0E9D;
            String var6;
            if (var9 > timeLimit) {
                this.b.makeMove(null, var7);
                var6 = "Time limit exceeded";
            } else {
                var6 = this.b.makeMove(var1, var7);
            }

            if (batch || log) {
                System.out.println(colorToString(var7) + ": " + var1);
                if (this.b.getWinner() != null) {
                    System.out.println(var6 + "\n" + colorToString(this.b.getWinner()) + " WINS");
                }
            }

            if (!batch) {
                this.statusLabel.setText(var6);
                this.b.repaint();
            }

            if (this.pause && var9 < pauseTime) {
                try {
                    Thread var10000 = this.playThread;
                    Thread.sleep((long)((int)((pauseTime - var9) * 1000.0D)));
                } catch (InterruptedException var13) {
                }
            }
        }

        this.playThread = null;
    }

    public void update() {
        this.b.repaint();
    }

    public boolean mouseDown(Event var1, int var2, int var3) {
        if (var1.target == this.b && this.b.getWinner() == null) {
            int var10000 = var2 - 1;
            GomokuBoard var10001 = this.b;
            int var4 = var10000 / 80;
            var10000 = var3 - 1;
            var10001 = this.b;
            int var5 = var10000 / 80;
            if (this.b.getTurn() == Color.white && this.whitePlayer instanceof HumanPlayer) {
                ((HumanPlayer)this.whitePlayer).clicked = new Move(var5, var4);
                synchronized(this.whitePlayer) {
                    this.whitePlayer.notifyAll();
                }
            } else if (this.b.getTurn() == Color.black && this.blackPlayer instanceof HumanPlayer) {
                ((HumanPlayer)this.blackPlayer).clicked = new Move(var5, var4);
                synchronized(this.blackPlayer) {
                    this.blackPlayer.notifyAll();
                }
            }
        }

        return true;
    }

    public boolean action(Event var1, Object var2) {
        if (var1.target == this.startButton) {
            if (this.playThread != null) {
                this.playThread.stop();
            }

            this.init();
            this.playThread = new Thread(this);
            this.playThread.start();
        }

        if (var1.target == this.quitButton) {
            this.setVisible(false);
            this.dispose();
            System.exit(0);
        }

        return true;
    }

    public static String colorToString(Color var0) {
        if (var0 == null) {
            return "NULL";
        } else if (var0 == Color.white) {
            return "White";
        } else {
            return var0 == Color.black ? "Black" : "None";
        }
    }

    public static void main(String[] args) {
        for(int i = 0; i < args.length; ++i) {
            if (args[i].equals("batchTest")) {
                batch = true;
            } else if (args[i].equals("log")) {
                log = true;
            } else if (args[i].equals("limit") && i + 1 < args.length) {
                ++i;
                timeLimit = Double.parseDouble(args[i]);
            } else if (args[i].equals("delay") && i + 1 < args.length) {
                ++i;
                pauseTime = Double.parseDouble(args[i]);
            }
        }

        GomokuReferee referee = new GomokuReferee();
        if (batch) {
            int[][] results = new int[referee.playerCount][referee.playerCount];

            int i;
            int j;
            int var5;
            int moves;
            int var7;
            for(i = 0; i < referee.playerCount; ++i) {
                for(j = 0; j < referee.playerCount; ++j) {
                    if (args.length != 2 || args[1].equals(referee.whiteChoice.getItem(i)) || args[1].equals(referee.whiteChoice.getItem(j))) {
                        System.out.println("White: " + referee.whiteChoice.getItem(i) + " vs Black: " + referee.whiteChoice.getItem(j));

                        try {
                            results[i][j] = 0;
                            referee.whitePlayer = (GomokuPlayer)referee.players[i].newInstance();
                            results[i][j] = 2;
                            referee.blackPlayer = (GomokuPlayer)referee.players[j].newInstance();
                        } catch (Exception var11) {
                            System.out.println(var11);
                            System.out.println(results[i][j] == 0 ? "Black WINS" : "White WINS");
                            continue;
                        }

                        referee.playThread = new Thread(referee);
                        referee.b.init();
                        referee.playThread.start();
                        moves = referee.b.getMoveCount();
                        var7 = 0;

                        while(referee.b.getWinner() == null) {
                            try {
                                Thread.currentThread();
                                Thread.sleep(200L);
                                ++var7;
                            } catch (InterruptedException var10) {
                            }

                            var5 = referee.b.getMoveCount();
                            if (var5 > moves) {
                                moves = var5;
                                var7 = 0;
                            } else if ((double)var7 / 5.0D > timeLimit) {
                                if (referee.b.getWinner() == null) {
                                    System.out.println("No progress");
                                    referee.playThread.suspend();
                                    if (referee.b.getWinner() != null) {
                                        referee.playThread.resume();
                                    } else {
                                        referee.playThread.stop();
                                        referee.b.makeMove(null, referee.b.getTurn());
                                        System.out.println("Time limit exceeded\n" + colorToString(referee.b.getWinner()) + " WINS");
                                    }
                                } else {
                                    moves = var5;
                                }
                            }
                        }

                        if (referee.b.getWinner() == Color.white) {
                            results[i][j] = 2;
                        } else if (referee.b.getWinner() == Color.black) {
                            results[i][j] = 0;
                        } else {
                            results[i][j] = 1;
                        }
                    }
                }
            }

            for(i = 0; i < referee.playerCount; ++i) {
                j = 0;
                var5 = 0;
                moves = 0;
                var7 = 0;
                int var8 = 0;

                for(int k = 0; k < referee.playerCount; ++k) {
                    j += results[i][k];
                    var5 += 2 - results[k][i];
                    if (results[i][k] == 2) {
                        ++moves;
                    }

                    if (results[k][i] == 0) {
                        ++moves;
                    }

                    if (results[i][k] == 1) {
                        ++var7;
                    }

                    if (results[k][i] == 1) {
                        ++var7;
                    }

                    if (results[i][k] == 0) {
                        ++var8;
                    }

                    if (results[k][i] == 2) {
                        ++var8;
                    }
                }

                System.out.println(referee.whiteChoice.getItem(i) + " " + moves + " " + var7 + " " + var8 + " " + j + " " + var5 + " " + (j + var5));
            }

            System.exit(0);
        }

    }
}
