import threading, SocketServer
from plyer.facades import Accelerometer, Compass, Gyroscope
from libs.server.device_handler import DeviceHandler, parse_message

class AccHandler( DeviceHandler ):
    device_name  = 'Accel'
    device_class = Accelerometer
    parser       = parse_message

class CompassHandler( DeviceHandler ):
    device_name  = 'Compass'
    device_class = Compass
    parser       = parse_message

class GyroHandler( DeviceHandler ):
    device_name  = 'Gyro'
    device_class = Gyroscope
    parser       = parse_message

PORTNO = 10552

class sManager(threading.Thread):
    def __init__(self):
        threading.Thread.__init__(self)
        self.handler = None
        self.device_caches = {}
        self.add_handler( AccHandler )
        self.add_handler( CompassHandler )
        self.add_handler( GyroHandler )

    def add_handler( self, handler ):
        # Create an entry in the device_caches dictionary using the handler.device_class class as a key.
        self.device_caches[ handler.device_class ] = []

        # Set handler.device_cache (the cache of recieved data) to that entry.
        handler.device_cache = self.device_caches[ handler.device_class ] 

        if self.handler:
            # Keep a reference to the old handler.
            old_handler = self.handler

            # Aggregate the handlers: instantiate an old handler and a new handler for each received datagram.
            def aggregate_handler(request, client_address, obj):
                old_handler(request, client_address, obj)
                handler(request, client_address, obj)

            self.handler = aggregate_handler
        else:
            self.handler = handler

    def run(self):
        # Build a new server configured with self.handler.
        self.server = SocketServer.UDPServer(('',PORTNO), self.handler)

        def server_in_sandbox():
            try: self.server.serve_forever()
            except: return 

        # Run the server and ignore any errors emanating from the server.
        server_in_sandbox()

    def get_device_cache( self, device_class ):
        try:
            # If all goes right, this will have been populated by the handler associated with device_class.
            return self.device_caches[ device_class ]
        except KeyError:
            # Display an error before returning bad data (return type should be an iterable).
            print "There is no device cache setup for {}.".format( device_class )
            return  None

    def shutdown( self ):
        # Shutdown self.server, thereby killing the thread.
        self.server.shutdown()
        # Free up socket address.
        self.server.server_close()
