from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.clock import Clock
from kivy.properties import StringProperty

from kivy.lang import Builder
Builder.load_file( 'libs/device/devicetest.kv' )

class DeviceTest(BoxLayout):
    # Because self.fullname is accessed in the .kv file, and the .kv file loads before __init__ is called,
    # a Property is used. When the value of self.fullname is later changed by __init__, the attribute of the widget 
    # who referenced self.fullname in the .kv will be updated.
    fullname = StringProperty('')

    def __init__(self, name, fullname, device, retrieval_func, **kwargs):
        super(type(self), self).__init__(**kwargs)
        self.name = name
        self.fullname = fullname
        self.device = device
        self.retrieve_data = retrieval_func
        self.sensorEnabled = False

    def enabled( self ): 
        return self.sensorEnabled

    def do_toggle(self):
        try:
            if not self.sensorEnabled:
                self.device.enable()
                Clock.schedule_interval(self.update_labels, 1 / 20.)

                self.sensorEnabled = True
                self.ids.toggle_button.text = "Stop " + self.fullname
            else:
                self.device.disable()
                Clock.unschedule(self.update_labels)

                self.sensorEnabled = False
                self.ids.toggle_button.text = "Start " + self.fullname
        except NotImplementedError:
            import traceback; traceback.print_exc()
            self.ids.device_status.text = self.fullname + " is not implemented for your platform"
            
    def update_labels(self, dt):
        x, y, z = self.retrieve_data()[:3]
        if (x, y, z) != (None, None, None):
            self.ids.x_label.text = "X: " + str( x )
            self.ids.y_label.text = "Y: " + str( y )
            self.ids.z_label.text = "Z: " + str( z )

