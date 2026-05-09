package utils;

import models.Point;

public class SvgGenerator {

    public static String generateSvg(Point point, double r) {
        int width = 400;
        int height = 400;
        int centerX = width / 2;
        int centerY = height / 2;
        double scale = 30;

        int rectSize = (int)(r * scale);
        int triangleBase = (int)(r * scale);
        int triangleHeight = (int)(r * scale / 2);
        int circleRadius = (int)(r * scale / 2);

        int pointX = centerX + (int)(point.getX() * scale);
        int pointY = centerY - (int)(point.getY() * scale);

        return String.format(
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                        "<svg width=\"%d\" height=\"%d\" xmlns=\"http://www.w3.org/2000/svg\">" +

                        "<!-- Background -->" +
                        "<rect width=\"100%%\" height=\"100%%\" fill=\"#f8f9fa\"/>" +

                        "<!-- Coordinate system -->" +
                        "<line x1=\"0\" y1=\"%d\" x2=\"%d\" y2=\"%d\" stroke=\"#ccc\" stroke-width=\"1\"/>" +
                        "<line x1=\"%d\" y1=\"0\" x2=\"%d\" y2=\"%d\" stroke=\"#ccc\" stroke-width=\"1\"/>" +

                        "<!-- Shapes -->" +
                        "<rect x=\"%d\" y=\"%d\" width=\"%d\" height=\"%d\" fill=\"rgba(0,123,255,0.3)\" stroke=\"#007bff\"/>" +
                        "<polygon points=\"%d,%d %d,%d %d,%d\" fill=\"rgba(40,167,69,0.3)\" stroke=\"#28a745\"/>" +
                        "<path d=\"M %d %d L %d %d A %d %d 0 0 1 %d %d Z\" fill=\"rgba(220,53,69,0.3)\" stroke=\"#dc3545\"/>" +

                        "<!-- Point -->" +
                        "<circle cx=\"%d\" cy=\"%d\" r=\"5\" fill=\"%s\" stroke=\"#000\"/>" +

                        "<!-- Labels -->" +
                        "<text x=\"10\" y=\"20\" font-family=\"Arial\" font-size=\"14\" fill=\"#333\">" +
                        "Point: (%.2f, %.2f), R: %.1f, Hit: %s" +
                        "</text>" +

                        "</svg>",

                width, height,
                centerY, width, centerY,
                centerX, centerX, height,
                centerX, centerY - rectSize, rectSize, rectSize,
                centerX, centerY, centerX - triangleBase, centerY, centerX, centerY + triangleHeight,
                centerX, centerY, centerX + circleRadius, centerY, circleRadius, circleRadius, centerX, centerY + circleRadius,
                pointX, pointY, point.isStatus() ? "#28a745" : "#dc3545",
                point.getX(), point.getY(), r, point.isStatus() ? "Yes" : "No"
        );
    }
}