from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.core.window import Window 
w, h = Window.width, Window.height

from libs.swipeplane2 import SwipePlane
from libs.device.device_view import DeviceTest
from libs.client.client_view import Client

from plyer import accelerometer, compass, gyroscope
from plyer.libs.server_utils import shutdown_server_thread

class MobileSensorTest(BoxLayout):
    def __init__(self):
        super(MobileSensorTest, self).__init__()
        self.add_widget( DeviceTest( 'Accel', 'Accelerometer', accelerometer, accelerometer._get_acceleration ) )
        self.add_widget( DeviceTest( 'Compass', 'Compass', compass, compass._get_orientation ) )
        self.add_widget( DeviceTest( 'Gyro', 'Gyroscope', gyroscope, gyroscope._get_orientation ) )

    def input_sources( self ):
        return self.children

class MobileSensorTestApp(App):

    def on_pause(self):
        return True

    def on_resume(self):
        pass

    def on_stop(self):
        shutdown_server_thread()

    def build(self):
        swipe_plane = SwipePlane()

        self.mobile_sensor_test = MobileSensorTest() 
        page1 = BoxLayout( pos=(0,0), size=(w, h) )
        page1.add_widget( self.mobile_sensor_test )
        swipe_plane.add_page( page1 )
        
        page2 = BoxLayout( pos=(1.2*w,0), size=(w, h) )
        page2.add_widget( Client( self.mobile_sensor_test.input_sources(), cols=1 ) )
        swipe_plane.add_page( page2 )

        return swipe_plane

if __name__ == '__main__':
    MobileSensorTestApp().run()
