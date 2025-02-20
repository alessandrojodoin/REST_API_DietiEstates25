package it.unina.rest_api_dietiestates25;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.awt.*;

@Converter(autoApply = true)
public static class ImageConverter
        implements AttributeConverter<Image, byte[]> {
    @Override
    public byte[] convertToDatabaseColumn(Image image) {

    }

    @Override
    public EnumSet<DayOfWeek> convertToEntityAttribute(Integer encoded) {
        var set = EnumSet.noneOf(DayOfWeek.class);
        var values = DayOfWeek.values();
        for (int i = 0; i<values.length; i++) {
            if (((1<<i) & encoded) != 0) {
                set.add(values[i]);
            }
        }
        return set;
    }
}
