package com.example.uberv.myopenglapplication;

import android.content.Context;

import static android.opengl.GLES20.GL_COMPILE_STATUS;
import static android.opengl.GLES20.GL_LINK_STATUS;
import static android.opengl.GLES20.glAttachShader;
import static android.opengl.GLES20.glCompileShader;
import static android.opengl.GLES20.glCreateProgram;
import static android.opengl.GLES20.glCreateShader;
import static android.opengl.GLES20.glDeleteProgram;
import static android.opengl.GLES20.glDeleteShader;
import static android.opengl.GLES20.glGetProgramiv;
import static android.opengl.GLES20.glGetShaderiv;
import static android.opengl.GLES20.glLinkProgram;
import static android.opengl.GLES20.glShaderSource;


/**
 * Подготовка шейдеров к использованию
 */
public class ShaderUtils {

    /**
     * Создайт программу: пара шейдеров - вершинный и фрагментный)
     * Эта пара шейдеров должна работать в связке -
     * первый отвечает за вершины, а второй - за цвета
     * @param vertexShaderId ид вершинного шейдера
     * @param fragmentShaderId ид фрагментного шейдера
     * @return
     */
    public static int createProgram(int vertexShaderId, int fragmentShaderId) {
        // создайт пустую программу
        final int programId = glCreateProgram();
        if (programId == 0) {
            // что то пошло не так
            return 0;
        }
        // привязываем шейдеры к программе
        glAttachShader(programId, vertexShaderId);
        glAttachShader(programId, fragmentShaderId);
        // формируем программу из привязанных шейдеров
        glLinkProgram(programId);
        // проверяем статус программы
        final int[] linkStatus = new int[1];
        glGetProgramiv(programId, GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == 0) {
            glDeleteProgram(programId);
            return 0;
        }
        // программа готова
        return programId;
    }

    static int createShader(Context context, int type, int shaderRawId) {
        String shaderText = FileUtils.readTextFromRaw(context, shaderRawId);
        return ShaderUtils.createShader(type, shaderText);
    }

    /**
     * Этот метод принимает на вход тип шейдера и его содержимое в виде строки, и далее вызывает кучу OpenGL методов по созданию и компиляции шейдера
     * @param type
     * @param shaderText
     * @return
     */
    static int createShader(int type, String shaderText) {
        // создает пустой объект шейдера и возвращает его id в переменную shaderId.
        // На вход принимает тип шейдера: GL_VERTEX_SHADER (вершинный) или GL_FRAGMENT_SHADER (фрагментный).
        // Вернет 0 если по каким-то причинам шейдер создать не удалось.
        final int shaderId = glCreateShader(type);
        if (shaderId == 0) {
            return 0;
        }

        // берет исходник шейдера из строки и ассоциирует его с шейдером shaderId.
        glShaderSource(shaderId, shaderText);
        // компилирует шейдер shaderId
        glCompileShader(shaderId);

        final int[] compileStatus = new int[1];
        // получаем статус компиляции в compileStatus
        glGetShaderiv(shaderId, GL_COMPILE_STATUS, compileStatus, 0);
        // 1 - GL_TRUE
        // 0 - GL_FALSE
        if (compileStatus[0] == 0) {// GL_FALSE
            glDeleteShader(shaderId);
            return 0;
        }

        // шейдер готов к работе и у нас есть его id
        return shaderId;
    }
}
