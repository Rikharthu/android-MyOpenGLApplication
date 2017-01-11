package com.example.uberv.myopenglapplication;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.GL_COLOR_BUFFER_BIT;
import static android.opengl.GLES20.GL_FLOAT;
import static android.opengl.GLES20.GL_FRAGMENT_SHADER;
import static android.opengl.GLES20.GL_LINES;
import static android.opengl.GLES20.GL_LINE_LOOP;
import static android.opengl.GLES20.GL_POINTS;
import static android.opengl.GLES20.GL_TRIANGLES;
import static android.opengl.GLES20.GL_TRIANGLE_FAN;
import static android.opengl.GLES20.GL_TRIANGLE_STRIP;
import static android.opengl.GLES20.GL_VERTEX_SHADER;
import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;
import static android.opengl.GLES20.glDrawArrays;
import static android.opengl.GLES20.glEnableVertexAttribArray;
import static android.opengl.GLES20.glGetAttribLocation;
import static android.opengl.GLES20.glGetUniformLocation;
import static android.opengl.GLES20.glLineWidth;
import static android.opengl.GLES20.glUniform4f;
import static android.opengl.GLES20.glUseProgram;
import static android.opengl.GLES20.glVertexAttribPointer;
import static android.opengl.GLES20.glViewport;


public class OpenGLRenderer implements Renderer {
    public static final String LOG_TAG=OpenGLRenderer.class.getSimpleName();

    private Context context;
    private int programId;
    private FloatBuffer vertexData;
//    private int uColorLocation;
    private int aColorLocation;
    private int aPositionLocation;


    public OpenGLRenderer(Context context) {
        this.context = context;
        prepareData();
    }

    // вызывается при создании/пересоздании surface
    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        // инициализация графический обьектов и установка OpenGL параметров

        // устанавливаем дефолтный цвет
        glClearColor(0f, 0f, 0f, 1f);
        // создайм шейдеры и программу
        int vertexShaderId = ShaderUtils.createShader(context, GL_VERTEX_SHADER, R.raw.vertex_shader);
        int fragmentShaderId = ShaderUtils.createShader(context, GL_FRAGMENT_SHADER, R.raw.fragment_shader);
        programId = ShaderUtils.createProgram(vertexShaderId, fragmentShaderId);
        // сообщаем системе, что эту программу надо использовать для построения изображения
        glUseProgram(programId);
        // передаёт данные в шейдеры
        //bindData();
        bindData2();

    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        // задаём область surface
        glViewport(0, 0, width, height);
    }

    // вызывается, когда surface готово отобразить очередной кадр.
    @Override
    public void onDrawFrame(GL10 gl10) {
        glClear(GL_COLOR_BUFFER_BIT);
        glLineWidth(5);
        /*
        // синие треугольники
        glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        glDrawArrays(GL_TRIANGLES, 0, 12);

        // зеленая линия
        glUniform4f(uColorLocation, 0.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 12, 2);

        // желтая линия
        glUniform4f(uColorLocation, 1.0f, 1.0f, 0.0f, 1.0f);
        glDrawArrays(GL_LINES, 14, 2);

        // красные точки
        glUniform4f(uColorLocation, 1.0f, 0.0f, 0.0f, 1.0f);
        glDrawArrays(GL_POINTS, 16, 3);
        */
        glLineWidth(5);
        glDrawArrays(GL_TRIANGLES, 0, 3);;

    }

    /**
     * Подготавливаем данные для передачи в шейдеры
     */
    private void prepareData() {
        // 6 элементов - координаты трёх точек [x1,y1,x2,y2,x3,y3]
        /*
        float[] vertices = {
                // треугольник 1
                -0.9f, 0.8f,
                -0.9f, 0.2f,
                -0.5f, 0.8f,

                // треугольник 2
                -0.6f, 0.2f,
                -0.2f, 0.2f,
                -0.2f, 0.8f,

                // треугольник 3
                0.1f, 0.8f,
                0.1f, 0.2f,
                0.5f, 0.8f,

                // треугольник 4
                0.1f, 0.2f,
                0.5f, 0.2f,
                0.5f, 0.8f,

                // линия 1
                -0.7f, -0.1f,
                0.7f, -0.1f,

                // линия 2
                -0.6f, -0.2f,
                0.6f, -0.2f,

                // точка 1
                -0.5f, -0.3f,

                // точка 2
                0.0f, -0.3f,

                // точка 3
                0.5f, -0.3f,
        };
        */
        float[] vertices = {
                -0.5f, -0.2f, 1.0f, 0.0f, 0.0f,
                0.0f, 0.2f, 0.0f, 1.0f, 0.0f,
                0.5f, -0.2f, 0.0f, 0.0f, 1.0f,
        };
        // конвертируем в буффер (размер float - 4 байта => *4
        vertexData = ByteBuffer.allocateDirect(vertices.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        vertexData.put(vertices);
    }


    /**
     * Передать данные в шейдеры
     */
    private void bindData() {
        // получаем положение в шейдере нашей uniform переменнеой u_Color
        // (uniform vec4 u_Color из fragment_shader.glsl)
        //uColorLocation = glGetUniformLocation(programId, "u_Color");
        // заполняем эту переменную 4-мя значениями (R,G,B,A)
        //glUniform4f(uColorLocation, 0.0f, 0.0f, 1.0f, 1.0f);
        // аналогично получаем положение переменной a_Position
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        // сообщаем, что данные из vertexData надо читать начиная с элемента по индексу 0 (с начала)
        vertexData.position(0);
        // сообщаем, что шейдеру для своего аттрибута a_Position необходимо читать данные из массива vertexData
        // и задаём правила чтения
        /* size=2 => указываем, что надо брать по 2 зачения из vertexData чтобы заполнить аттрибут
         * на позиции aPositionLocation (a_Position) float данными:
         *  Первый запуск: v1, v2, 0, 1
            Второй запуск: v3, v4, 0, 1
            Третий запуск: v5, v6, 0 ,1
           Третий и четвертый элементы беруться дефолтными, т.к. туда ничего не передаём */
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 0, vertexData);
        // включить аттрибут
        glEnableVertexAttribArray(aPositionLocation);
    }

    private void bindData2(){
        // координаты
        aPositionLocation = glGetAttribLocation(programId, "a_Position");
        /* Что сделает stride = 20?
        1) позиция в массиве vertexData ставится в 0, т.е. на первый элемент
        2) система берет 2 float значения (т.е. координаты вершины) из vertexData и передает их
           в aPositionLocation (что соответствует атрибуту a_Position в вершинном шейдере)
        3) позиция перемещается на 20 байтов, т.е. к координатам следующей вершины.
        * Почему 20? у нас вершина состоит из 5 float - x,y, R,G,B, 5*4bytes=20bytes*/
        vertexData.position(0);
        glVertexAttribPointer(aPositionLocation, 2, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aPositionLocation);

        // цвет
        aColorLocation = glGetAttribLocation(programId, "a_Color");
        // начиная со второй позиции (пропустим x, y) читаем три RGB компоменнта цвета
        vertexData.position(2);
        glVertexAttribPointer(aColorLocation, 3, GL_FLOAT, false, 20, vertexData);
        glEnableVertexAttribArray(aColorLocation);
    }

}
