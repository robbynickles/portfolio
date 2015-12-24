import SocketServer

def parse_message( handler, msg ):
    """ Tell the handler how to parse the message. """
    # Message format: 
    # "Device_Name, x, y, z" 
    try:
        device_name, x, y, z = msg.split(',')
        return device_name, x, y, z
    except ValueError:
        print "Recieved message ( {} ) couldn't be split into (device_name, x, y, z)".format( msg )
        return None, 0,0,0

class DeviceHandler(SocketServer.DatagramRequestHandler):
    device_name  = None
    device_class = None
    device_cache = []
    parser       = lambda handler, msg: (None, 0, 0, 0)

    def handle(self):
        newmsg = self.request[ 0 ]
        try:
            dev, x, y, z = self.parser( newmsg )
        except ValueError:
            print """Parser returns something of the wrong format.
            It should return a 4-tuple, preferably of the form (device_name, x, y, z)."""
            return
        # Only relay messages that are labeled self.device_name.
        if dev == self.device_name:
            try:
                x, y, z = map(float, [x, y, z] )
                # Clear the cache when it gets too big, but never empty it.
                if len(self.device_cache) < 100:
                    self.device_cache.append( (x, y, z) )
                else:
                    # Since self.device_cache originally was passed in as an argument to __init__, it refers to something in the caller's
                    # context. Creating a new reference loses part of self.device_cache's context, meaning future changes to 
                    # self.device_cache here wouldn't change the thing the original reference pointed to. The line 'self.device_cache = []' 
                    # would effectively pause the input to the caller. So the cache has to be cleared in-place. 

                    for item in self.device_cache[:-1]:#Leave the most recent item.
                        self.device_cache.pop( 0 )
            except ValueError:
                print "{} messsage wrong format: the last three values couldn't be converted floats.".format( self.device_name )
                return
