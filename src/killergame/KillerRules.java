package killergame;

import java.awt.geom.Area;
import java.util.ArrayList;

public class KillerRules {
    
    //meth
    public static boolean testCollision(VisualObject object, ArrayList<VisualObject> objects) {
        boolean result = false;
        //recorre la array te Visual Objects
        for (int i = 0; i < objects.size(); i++) {
            VisualObject objectsT = objects.get(i);
            if (!object.equals(objectsT)) {//not the same object
                Area area = object.getArea();
                area.intersect(objectsT.getArea()); //2 objects intersect
                if (!area.isEmpty()) {

                    result = true;
                    if (objectsT instanceof Alive) {
                        Alive alive = (Alive) objectsT;
                        ((Alive) objectsT).collision();
                        System.out.println("collision");
                    }
                }
            }

        }

        return result;
    }
}

