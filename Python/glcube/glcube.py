#!/usr/bin/env python

import pygame
from pygame.locals import *

from plyer import gyroscope
from plyer import accelerometer
from drawings import drawcube, drawaxes

try:
    from OpenGL.GL import *
    from OpenGL.GLU import *
except:
    print ('The GLCUBE example requires PyOpenGL')
    raise SystemExit

def main():
    "run the demo"
    #initialize pygame and setup an opengl display
    SIZE = W, H = 1200, 700
    pygame.init()
    pygame.display.set_mode((W,H), OPENGL|DOUBLEBUF)
    glEnable(GL_DEPTH_TEST)        #use our zbuffer

    #setup the camera
    glMatrixMode(GL_PROJECTION)
    gluPerspective(45.0,W/float(H),0.1,100.0)   #setup lens
    glTranslatef(0.0, 0.0, -3.0)                #move back
    glRotatef(60, 1, 0, 0)                      #orbit higher

    # Enable sensors.
    gyroscope.enable()
    accelerometer.enable()

    # Wait for accelerometer to setup.
    x_acc, y_acc, z_acc = accelerometer.acceleration[:3]
    while (x_acc, y_acc, z_acc) == (0,0,0):
        x_acc, y_acc, z_acc = accelerometer.acceleration[:3]

    # Adjust initial orientation by initial acceleration vector on x-axis and z-axis.
    # This acceleration vector contains the gravity acceleration vector and the user acceleration vector.
    # It is primarily gravity if the user isn't accelerating. 
    glRotatef(-90.*x_acc,0,0,1)# x_acc<0 means positive angle away from starting orientation.
    glRotatef( 90.*z_acc,1,0,0)

    doDrawAxes = False
    # Animation Loop
    while 1:
        event = pygame.event.poll()
        if event.type == QUIT or (event.type == KEYDOWN and event.key == K_ESCAPE):
            # Disable sensors.
            gyroscope.disable()
            accelerometer.disable()
            # Break out of the animation loop.
            break
        if event.type == KEYDOWN and event.key == K_a:
            doDrawAxes = not doDrawAxes
            
        #clear screen and move camera
        glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT)

        # Render shapes
        drawcube()
        if doDrawAxes:
            drawaxes()            
        
        # Gather gyroscope orientation.
        x_rot, y_rot, z_rot = gyroscope.orientation[:3]

        #x_acc, y_acc, z_acc = accelerometer.acceleration[:3]
        
        x_ang, y_ang, z_ang = x_rot, y_rot, z_rot

        # Rotate using gyroscope data.
        glRotatef(x_ang,1,0,0)
        glRotatef(y_ang,0,1,0)
        glRotatef(z_ang,0,0,1)
        
        pygame.display.flip()
        pygame.time.wait(10)


if __name__ == '__main__': 
    main()
