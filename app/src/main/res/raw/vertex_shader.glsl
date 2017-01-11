// вершинный шейдер
attribute vec4 a_Position;
attribute vec4 a_Color;
// сюда будет передаваться цвет для каждой вершины
varying vec4 v_Color;
/* varying переменные используются для обмена данными между шейдерами
v_Color мы заполняем в вершинном шейдере а используем в фрагментном */

void main() {
    gl_Position = a_Position;
    gl_PointSize = 5.0;
    //
    v_Color = a_Color;
}