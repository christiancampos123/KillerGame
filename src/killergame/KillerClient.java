package killergame;

public class KillerClient implements Runnable {

    //Attr
    private VisualHandler vh;
    private long refresh;
    private KillerPad kPad;

    //Const
    public KillerClient(VisualHandler vh, long refresh) {
        this.vh = vh;
        this.refresh = refresh;
    }

    public KillerClient(KillerPad kPad, long refresh) {
        this.kPad = kPad;
        this.refresh = refresh;
    }
    //Meth

    @Override
    public void run() {
        while (true) {
            //modulo
            if (this.vh != null && !this.vh.connected() && this.vh.getHost() != null) {
                this.vh.contact();
            }

            //gamepad
            if (this.kPad != null && !this.kPad.connected() && this.kPad.getHost() != null) {
                this.kPad.contact();
            }

            try {
                Thread.sleep(this.refresh);
            } catch (InterruptedException e) {

            }
        }
    }

}