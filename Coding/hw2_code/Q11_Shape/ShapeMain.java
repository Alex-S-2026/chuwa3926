public class ShapeMain {
    public static void main(String[] args) {
        Shape[] shapes = new Shape[] {
                new Rectangle("Red", 4.0d, 3.0d),
                new Circle("Blue", 2.0d)
        };

        for (Shape s : shapes) {
            System.out.println(
                    s.getClass().getSimpleName()
                            + " (color=" + s.getColor() + ")"
                            + " area=" + s.getArea()
                            + " perimeter=" + s.getPerimeter()
            );

            if (s instanceof Drawable) {
                ((Drawable) s).draw();
            }
        }
    }
}