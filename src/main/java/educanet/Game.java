package educanet;


import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL33;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import educanet.models.Player;
import org.lwjgl.stb.STBImage;
import org.lwjgl.system.MemoryStack;

public class Game {

    // Player
    public static Player mainPlayer;
    public static float playerTopLeftX = -0.125f; //TODO automatizovat
    public static float playerTopLeftY = 0.125f;


    public static void init(long window) {
        // Setup shaders
        Shaders.initShaders();
        Player.loadImage();
        createPlayer();
    }

    public static void render(long window) {
        renderPlayer(Player.getMatrix());
    }

    public static void update(long window) {
        movePlayer(window, Player.getMatrix()); // change players position with WASD
    }

    public static void createPlayer() {
        mainPlayer = new Player();
    }

    public static void renderPlayer(Matrix4f matrix) {
        matrix.get(Player.getMatrixBuffer());

        GL33.glUniformMatrix4fv(Player.getUniformMatrixLocation(), false, Player.getMatrixBuffer());
        GL33.glUseProgram(Shaders.shaderProgramId); // use this shader to render
        GL33.glBindVertexArray(mainPlayer.getSquareVaoId());
        GL33.glDrawElements(GL33.GL_TRIANGLES, mainPlayer.getVertices().length, GL33.GL_UNSIGNED_INT, 0);
    }


    public static boolean right = true;
    public static boolean up = false;
    public static int timer = 0;
    static float baseSpeed = 0.0001f;
    static float slowSpeed = 0.75f * baseSpeed; // 0.00015f
    static float fastSpeed = baseSpeed; // 0.0002f



    public static void movePlayer(long window, Matrix4f matrix) {
            if (playerTopLeftX > 0.75f) { // on border hit flip x
                right = false;
            }
            else if (playerTopLeftX < -1f) { // on border hit flip x
                right = true;
            }

            if (playerTopLeftY > 1f) { // on border hit flip y
                up = false;
            }
            else if (playerTopLeftY < -0.75f) { // on border hit flip y
                up = true;
            }

            if (!right && !up) { // left down
                matrix = matrix.translate(-fastSpeed, -slowSpeed, 0f);
                playerTopLeftX -= fastSpeed;
                playerTopLeftY -= slowSpeed;
            }
            else if (!right && up) { // left up
                matrix = matrix.translate(-fastSpeed, slowSpeed, 0f);
                playerTopLeftX -= fastSpeed;
                playerTopLeftY += slowSpeed;
            }
            else if (right && !up) { // right down
                matrix = matrix.translate(fastSpeed, -slowSpeed, 0f);
                playerTopLeftX += fastSpeed;
                playerTopLeftY -= slowSpeed;
            }
            else { // right up
                matrix = matrix.translate(fastSpeed, slowSpeed, 0f);
                playerTopLeftX += fastSpeed;
                playerTopLeftY += slowSpeed;
            }

        timer++;
        if (timer % 100 == 0) {
            System.out.println("X: " + playerTopLeftX + "\t|\tY: " + playerTopLeftY);
            timer = 0;
        }
    }

}
