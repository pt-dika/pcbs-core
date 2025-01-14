package dk.apps.pcps.commonutils.print;


import java.awt.*;


public class MultipleReturnResult {
    private final Graphics2D G2D;
    private final int RowIndex;

    public MultipleReturnResult(Graphics2D graph2D, int iRowIdx) {
        this.G2D = graph2D;
        this.RowIndex = iRowIdx;
	}

    public Graphics2D getGraph2D() {
        return G2D;
    }

    public int getRowIdx() {
        return RowIndex;
    }
}
