package it.unina.rest_api_dietiestates25.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Entity
public class FotoImmobile {


    @NotNull @Convert(converter = ImageConverter.class)
    private BufferedImage image;

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Immobile immobile;

    public BufferedImage getImage() {return image;}

    @Converter(autoApply = true)
    public static class ImageConverter
            implements AttributeConverter<BufferedImage, byte[]> {
        @Override
        public byte[] convertToDatabaseColumn(BufferedImage image){

            try{
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(image, "jpg", byteArrayOutputStream );
                return byteArrayOutputStream.toByteArray();
            }
            catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException("Error converting image to byte[]");
            }

        }

        @Override
        public BufferedImage convertToEntityAttribute(byte[] imageByteArray) {
            try{
                ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(imageByteArray);
                return ImageIO.read(byteArrayInputStream);
            }
            catch (IOException e){
                e.printStackTrace();
                throw new RuntimeException("Error converting byte[] to image");
            }


        }
    }
}
