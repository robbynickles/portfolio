from kivy.app import App
from kivy.uix.boxlayout import BoxLayout
from kivy.uix.label import Label
from kivy.clock import Clock

from kivy.lang import Builder
Builder.load_file( 'libs/client/client.kv' )

import socket

class Client(BoxLayout):
    """ UDP Client that will send data from input sources that have (x,y,z) data.  """
    HOSTADDR, PORT = '', 10552

    def __init__(self, input_sources, *args, **kwargs):
        super(Client, self).__init__( *args, **kwargs )
        self.clientEnabled = False
        self.input_sources = input_sources

        # Input-Source logs (IS_logs): each input source has its own log that displays its out messages.
        self.IS_logs = {}
        for input_source, out_log in zip( input_sources, self.ids.out_logs.children ):
            label = Label()
            self.IS_logs[ input_source.name ] = out_log
        
    def ip_entered(self, text_input ):
        text = text_input.text
        try:
            socket.inet_aton(text)
            # Legal
            self.HOSTADDR = text
            self.ids.status.text = "{} OK.".format( text )
        except socket.error:
            # Not legal
            self.ids.status.text = "{} is not a vaild IP address.".format( text )

    def my_IP( self ):
        # Not fully implemented. This only works sometimes.
        # Probe HOSTADDR to determine this device's IP within the network.
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.connect((self.HOSTADDR, 80))
        my_ip = s.getsockname()[0]
        s.close()
        return my_ip

    def do_toggle(self):
        try:
            if not self.clientEnabled:
                # Make sure the user has already specified HOSTADDR.
                if socket.inet_aton( self.HOSTADDR ):
                    # Create a UDP socket.
                    self.socket = socket.socket(socket.AF_INET, 
                                                socket.SOCK_DGRAM)#, socket.IPPROTO_UDP)
                    # Send 20 times a second.
                    Clock.schedule_interval(self.send_data, 1 / 20.)
                    
                    self.clientEnabled = True
                    self.ids.toggle_button.text = "Stop Client"
                else:
                    self.ids.status.text = "Please enter a destination IP address."
            else:
                Clock.unschedule(self.send_data)
                # Close the socket.
                self.socket.close()

                self.clientEnabled = False
                self.ids.toggle_button.text = "Start Client"
                self.ids.status.text = "Client inactive."
        except:
            pass

    def send_data(self, dt):
        # Display how many active inputs sources there are.
        srcs = len( filter( lambda mobile_input: mobile_input.enabled(), self.input_sources ) )
        self.ids.status.text = "Attempting to send data from {} input source{}.".format( srcs, '' if srcs==1 else 's')

        # Pull data from each motion-sensor's labels.
        for mobile_sensor in self.input_sources:

            # Only send data from a sensor when that sensor is turned on.
            if mobile_sensor.enabled():

                # The format of the message to be sent: "Device_Name, x, y, z"
                try:

                    # All labels are assumed to be strings of the form "A: f" where A is one of (X,Y,Z) and f is a string.
                    # Extract f from each label.
                    x = mobile_sensor.ids.x_label.text.split(':')[1]
                    y = mobile_sensor.ids.y_label.text.split(':')[1]
                    z = mobile_sensor.ids.z_label.text.split(':')[1]
                    msg = ','.join( [ mobile_sensor.name, x, y, z ] )
                except Exception as err:
                    # Either no value f is yet assigned to the label, 
                    # or mobile_sensor fails to access one, some or all of the labels.
                    # Fail silently, continue to the next mobile_sensor.
                    continue

                try:
                    # Send a UDP packet with the message string.
                    self.socket.sendto( msg, ( self.HOSTADDR, self.PORT ) )

                    # Display a success message containing the full string of the message sent.
                    log_text = "Successfully sent to {} at port {}:\n{}".format( self.HOSTADDR, self.PORT, msg )
                    self.IS_logs[ mobile_sensor.name ].text = log_text
                except Exception as err:
                    # Sending failed, display an error message.
                    err_msg = "Send to {} failed with error:\n{}.".format( self.HOSTADDR, err )
                    self.IS_logs[ mobile_sensor.name ].text = err_msg

                    # The socket could be broken. Create a new one.
                    self.socket.close()
                    self.socket = socket.socket(socket.AF_INET, 
                                                socket.SOCK_DGRAM)#, socket.IPPROTO_UDP)

