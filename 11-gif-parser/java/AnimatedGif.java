// Source from https://dukesoftware00.blogspot.com/2014/09/java-extract-gif-images-from-animated.html
package com.dukesoftware.utils.image;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class AnimatedGif {

    public static void main(String[] args) throws IOException {
        String sourceGif = "c:/temp/SmallFullColourGIF.gif";
        saveAnimatedGifFrameToImage(sourceGif, "c:/temp/gif_frames");
    }

    public static void saveAnimatedGifFrameToImage(String input, String outDir) throws IOException
    {
        final String[] imageatt = new String[]{
                "imageLeftPosition",
                "imageTopPosition",
        };
        ImageReader reader = ImageIO.getImageReadersByFormatName("gif").next();
        try(ImageInputStream ciis = ImageIO.createImageInputStream(new File(input)))
        {
            reader.setInput(ciis, false);

            BufferedImage master = null;

            for (int i = 0, noi = reader.getNumImages(true); i < noi; i++) {

                BufferedImage image = reader.read(i);

                if(i==0){
                    master = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
                }

                IIOMetadata metadata = reader.getImageMetadata(i);
                Map<String, Integer> imageAttr = extractMetaData(imageatt, metadata);

                Graphics graphics = master.getGraphics();
                Integer x = imageAttr.get("imageLeftPosition");
                Integer y = imageAttr.get("imageTopPosition");
                graphics.drawImage(image, x == null ? 0 : x , y == null ? 0 : y, null);
                ImageIO.write(master, "GIF", new File(new File(outDir), i + ".gif"));
            }
        }

    }

    private static Map<String, Integer> extractMetaData(String[] imageatt, IIOMetadata metadata) {
        Node tree = metadata.getAsTree("javax_imageio_gif_image_1.0");
        NodeList children = tree.getChildNodes();
        for (int j = 0; j < children.getLength(); j++) {
            Node nodeItem = children.item(j);
            if(nodeItem.getNodeName().equals("ImageDescriptor")){
                return createImageAttrMap(imageatt, nodeItem.getAttributes());
            }
        }
        return Collections.emptyMap();
    }

    private static Map<String, Integer> createImageAttrMap(String[] imageatt, NamedNodeMap attr) {
        Map<String, Integer> imageAttr = new HashMap<>();
        for (String att : imageatt) {
            Node attnode = attr.getNamedItem(att);
            if(attnode != null)
            {
                imageAttr.put(att, Integer.valueOf(attnode.getNodeValue()));
            }
        }
        return imageAttr;
    }
}
