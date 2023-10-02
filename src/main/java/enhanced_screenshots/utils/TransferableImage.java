package enhanced_screenshots.utils;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;

public final class TransferableImage
        implements Transferable {
    private final Image image;
    
    public TransferableImage(Image image) {this.image = image;}
    
    @Override
    public Object getTransferData(DataFlavor flavor)
            throws UnsupportedFlavorException {
        if (flavor.equals(DataFlavor.imageFlavor) && image != null) {
            return image;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
    
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        DataFlavor[] flavors = new DataFlavor[1];
        flavors[0] = DataFlavor.imageFlavor;
        return flavors;
    }
    
    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        DataFlavor[] flavors = getTransferDataFlavors();
        for (DataFlavor dataFlavor : flavors) {
            if (flavor.equals(dataFlavor)) {
                return true;
            }
        }
        return false;
    }
}