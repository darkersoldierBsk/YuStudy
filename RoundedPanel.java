import javax.swing.*;
import java.awt.*;

public class RoundedPanel extends JPanel {
    private int cornerRadius;
    private Color shadowColor = new Color(0, 0, 0, 40); // Softer, lighter shadow (Changed)
    private int shadowOffset = 4; // Reduced offset for a closer shadow (Changed)
    private int shadowSize = 15; // Softer shadow blur (Changed)

    public RoundedPanel(int radius) {
        super();
        this.cornerRadius = radius;
        setOpaque(false);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw subtle shadow (Updated for softer, lighter shadow)
        int shadowX = shadowOffset;
        int shadowY = shadowOffset;
        g2.setColor(shadowColor);
        g2.fillRoundRect(shadowX, shadowY, getWidth() - shadowOffset * 2, getHeight() - shadowOffset * 2, cornerRadius, cornerRadius);

        // Draw glass-like background (Reduced opacity for a cleaner look)
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.85f)); // Reduced opacity (Changed)
        g2.setColor(new Color(255, 255, 255, 220)); // Lighter, less opaque white (Changed)
        g2.fillRoundRect(0, 0, getWidth() - shadowOffset * 2, getHeight() - shadowOffset * 2, cornerRadius, cornerRadius);

        g2.dispose();
    }
}
