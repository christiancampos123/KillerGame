package killergame;

import java.util.ArrayList;

public class KillerAction {
    //meth
    
    //moves
    public static boolean mU(String host, ArrayList<VisualObject> objects) {
        
        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveUp();
            r = true;
        }

        return r;
    }
    
    public static boolean mNope(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveStop();
            r = true;
        }

        return r;
    }

    public static boolean mD(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveDown();
            r = true;
        }

        return r;
    }

    public static boolean mR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveRight();
            r = true;
        }

        return r;
    }

    public static boolean mL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveLeft();
            r = true;
        }

        return r;
    }

    public static boolean mUR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveUpRight();
            r = true;
        }

        return r;
    }

    public static boolean mUL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveUpLeft();
            r = true;
        }

        return r;
    }

    public static boolean mDR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveDownRight();
            r = true;
        }

        return r;
    }

    public static boolean mDL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.moveDownLeft();
            r = true;
        }

        return r;
    }

    //shots
    public static boolean sU(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotUp();
            r = true;
        }

        return r;
    }

    public static boolean sD(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotDown();
            r = true;
        }

        return r;
    }

    public static boolean sR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotRight();
            r = true;
        }

        return r;
    }

    public static boolean sL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotLeft();
            r = true;
        }

        return r;
    }

    public static boolean sUR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotUpRight();
            r = true;
        }

        return r;
    }

    public static boolean sUL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotUpLeft();
            r = true;
        }

        return r;
    }

    public static boolean sDR(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotDownRight();
            r = true;
        }

        return r;
    }

    public static boolean sDL(String host, ArrayList<VisualObject> objects) {

        boolean r = false;

        KillerShip ship = KillerAction.lookShip(host, objects);
        if (ship != null) {
            ship.shotDownLeft();
            r = true;
        }

        return r;
    }

    public static KillerShip lookShip(String host, ArrayList<VisualObject> objects) {

        KillerShip ks = null;

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i) instanceof KillerShip) {
                KillerShip ship = (KillerShip) objects.get(i);
                if (ship.getID().equals(host)) {
                    ks = ship;
                }
            }
        }

        return ks;

    }

}
