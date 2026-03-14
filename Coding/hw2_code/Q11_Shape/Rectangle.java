public class Rectangle extends Shape implements Drawable {
    private double width;
    private double height;

    public Rectangle(String color, double width, double height) {
        super(color);
        this.width = width;
        this.height = height;
    }

    @Override
    public double getArea() {
        return this.width * this.height;
    }

    @Override
    public double getPerimeter() {
        return 2 * (this.width + this.height);
    }

    @Override
    public void draw() {
        System.out.println("Drawing a " + getColor() + " rectangle (" + this.width + " x " + this.height + ")");
    }
}