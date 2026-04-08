package ru.slisarenko.geometryutils;

import java.util.Comparator;
import ru.slisarenko.geometrylibrary.AreaAndPerimeter;

public class ShapePerimeterComparator  implements Comparator<AreaAndPerimeter> {
    @Override
    public int compare(AreaAndPerimeter share1, AreaAndPerimeter share2) {
        return Double.compare(share1.calculatePerimeter(), share2.calculatePerimeter());
    }
}
