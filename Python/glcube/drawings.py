from pygame.locals import *

try:
    from OpenGL.GL import *
except:
    print ('The GLCUBE example requires PyOpenGL')
    raise SystemExit

CUBE_POINTS = (
    (0.3, -0.5, -0.1),  (0.3, 0.5, -0.1),
    (-0.3, 0.5, -0.1),  (-0.3, -0.5, -0.1),
    (0.3, -0.5, 0.1),   (0.3, 0.5, 0.1),
    (-0.3, -0.5, 0.1),  (-0.3, 0.5, 0.1)
)
SCREEN_FRAME = (
    (0.2, -0.4, 0.1),  (0.2, 0.4, 0.1),
    (-0.2, 0.4, 0.1),  (-0.2, -0.4, 0.1),
    (0.2, -0.4, 0.1),  (-0.2, -0.4, 0.1),
    (-0.2, 0.4, 0.1),  (0.2, 0.4, 0.1)
)
CUBE_COLORS = (
    (1, 0, 0), (1, 1, 0), (0, 1, 0), (0, 0, 0),
    (1, 0, 1), (1, 1, 1), (0, 0, 1), (0, 1, 1)
)
CUBE_QUAD_VERTS = (
    (0, 1, 2, 3), (3, 2, 7, 6), (6, 7, 5, 4),
    (4, 5, 1, 0), (1, 5, 7, 2), (4, 0, 3, 6)
)
CUBE_EDGES = (
    (0,1), (0,3), (0,4), (2,1), (2,3), (2,7),
    (6,3), (6,4), (6,7), (5,1), (5,4), (5,7),
)
def drawcube():
    "draw the cube"
    allpoints = zip(CUBE_POINTS, CUBE_COLORS)
    glBegin(GL_QUADS)
    for face in CUBE_QUAD_VERTS:
        for vert in face:
            pos, color = allpoints[vert]
            glColor3fv(color)
            glVertex3fv(pos)
    glEnd()
    white, black = (1.0,1.0,1.0), (0.0,0.0,0.0)
    glColor3f( *black )
    glBegin(GL_LINES)
    for line in CUBE_EDGES:
        for vert in line:
            pos, color = allpoints[vert]
            glVertex3fv(pos)
    for vert in SCREEN_FRAME:
        glVertex3fv(vert)
    glEnd()

def drawaxes():
    glColor3f(1.0, 0.0, 0.0)
    glBegin(GL_LINES)
    x_axis = [ (-5.0, 0.0, 0.0), (5.0, 0.0, 0.0) ]
    y_axis = [ (0.0, -5.0, 0.0), (0.0, 5.0, 0.0) ]
    z_axis = [ (0.0, 0.0, -5.0), (0.0, 0.0, 5.0) ]
    axis_colors = [color.THECOLORS['dodgerblue3'], color.THECOLORS['darkseagreen'], color.THECOLORS['indianred4'] ]
    axis_colors = [(r/255.0,g/255.0,b/255.0) for (r,g,b,x) in axis_colors]
    axes = zip([x_axis, y_axis, z_axis], axis_colors)
    for axis, c in axes:
        glColor3fv(c)
        for vertex in axis:
            glVertex3fv(vertex)
    glEnd()
