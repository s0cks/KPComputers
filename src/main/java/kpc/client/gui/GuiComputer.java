package kpc.client.gui;

import kpc.api.computer.Computer;
import kpc.client.gui.widget.WidgetTerminal;
import kpc.common.tile.TileEntityComputer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;

public final class GuiComputer
extends GuiScreen{
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;
    public static final ResourceLocation TEXTURE;
    static{
        try{
            TEXTURE = Minecraft.getMinecraft().renderEngine.getDynamicTextureLocation("kpc", new DynamicTexture(ImageIO.read(System.class.getResourceAsStream("/assets/kpc/textures/gui/display.png"))));
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    private final TileEntityComputer tile;
    private WidgetTerminal widgetTerminal;

    public GuiComputer(TileEntityComputer tile){
        this.tile = tile;
    }

    @Override
    public void initGui(){
        super.initGui();
        Keyboard.enableRepeatEvents(true);
        this.widgetTerminal = new WidgetTerminal(this.tile.createClientComputer());
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
        super.onGuiClosed();
    }

    @Override
    public void drawScreen(int mX, int mY, float f){
        Computer computer = this.tile.getComputer();
        this.drawDefaultBackground();
        ScaledResolution scaledResolution = new ScaledResolution(this.mc, this.mc.displayWidth, this.mc.displayHeight);
        int scaleFactor = scaledResolution.getScaleFactor();

        int dX = (this.mc.displayWidth / 2) - (SCREEN_WIDTH / 2);
        int dY = (this.mc.displayHeight / 2) - (SCREEN_HEIGHT / 2);

        GL11.glColor3f(1.0F, 1.0F, 1.0F);
        GL11.glScalef(4.0F / (float) scaleFactor, 4.0F / (float) scaleFactor, 0.0F);
        this.mc.renderEngine.bindTexture(TEXTURE);
        this.drawTexturedModalRect(dX / 4 - 8, dY / 4 - 8, 0, 0, 256, 256);
        this.drawColoredQuad(computer.terminal().getBackgroundColor(), dX / 4, dY / 4, this.zLevel, 200, 150);
        GL11.glScalef(0.25F, 0.25F, 0.0F);

        int x = dX / 4;
        int y = dY / 4;
        this.widgetTerminal.draw(x + 70, y + 10);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    @Override
    public void keyTyped(char c, int code){
        Computer computer = this.tile.createComputer();
        if(code == Keyboard.KEY_ESCAPE){
            this.mc.setIngameFocus();
            return;
        }

        if(code == Keyboard.KEY_RETURN){
            if(computer != null){
                computer.queueEvent("char", "__enter__", Keyboard.KEY_RETURN);
                return;
            }
        }

        if(code == Keyboard.KEY_BACK){
            if(computer != null){
                computer.queueEvent("char", "__back__", Keyboard.KEY_BACK);
                return;
            }
        }

        if(code == Keyboard.KEY_UP){
            if(computer != null){
                computer.queueEvent("char", "__up__", Keyboard.KEY_UP);
                return;
            }
        }

        if(code == Keyboard.KEY_DOWN){
            if(computer != null){
                computer.queueEvent("char", "__down__", Keyboard.KEY_DOWN);
                return;
            }
        }

        if(code == Keyboard.KEY_LEFT){
            if(computer != null){
                computer.queueEvent("char", "__left__", Keyboard.KEY_LEFT);
                return;
            }
        }

        if(code == Keyboard.KEY_RIGHT){
            if(computer != null){
                computer.queueEvent("char", "__right__", Keyboard.KEY_RIGHT);
                return;
            }
        }

        if(code == Keyboard.KEY_TAB){
            if(computer != null){
                computer.queueEvent("char", "__tab__", Keyboard.KEY_TAB);
                return;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && code == Keyboard.KEY_S){
            if(computer != null){
                computer.queueEvent("char", "__ctrls__");
                return;
            }
        }

        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && code == Keyboard.KEY_V){
            if(computer != null){
                computer.queueEvent("char", "__ctrlv__");
                return;
            }
        }

        if((code != Keyboard.KEY_LCONTROL && !Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) && ChatAllowedCharacters.isAllowedCharacter(c)){
            if(computer != null){
                computer.queueEvent("char", Character.toString(c), code);
                return;
            }
        }

        if(code == Keyboard.KEY_LCONTROL){
            if(computer != null){
                computer.queueEvent("char", "__ctrl__", Keyboard.KEY_LCONTROL);
            }
        }
    }

    private void drawColoredQuad(int color, int alpha, double x, double y, double z, double width, double height){
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();

        int r = (color >> 16 & 0xFF);
        int g = (color >> 8 & 0xFF);
        int b = (color & 0xFF);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tess.setColorRGBA(r, g, b, alpha);
        tess.addVertex(x, y + height, z);
        tess.addVertex(x + width, y + height, z);
        tess.addVertex(x + width, y, z);
        tess.addVertex(x, y, z);
        tess.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    private void drawColoredQuad(int color, double x, double y, double z, double width, double height){
        this.drawColoredQuad(color, 255, x, y, z, width, height);
    }
}