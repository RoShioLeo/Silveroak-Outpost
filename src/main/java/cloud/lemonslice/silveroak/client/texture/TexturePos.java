package cloud.lemonslice.silveroak.client.texture;

public record TexturePos(int textureX, int textureY, int width, int height)
{
    public int getX()
    {
        return textureX;
    }

    public int getY()
    {
        return textureY;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public static TexturePos create(int textureX, int textureY, int width, int height)
    {
        return new TexturePos(textureX, textureY, width, height);
    }
}
