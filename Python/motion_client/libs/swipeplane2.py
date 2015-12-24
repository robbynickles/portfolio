from kivy.uix.scatter import ScatterPlane
from kivy.graphics.transformation import Matrix 
from kivy.uix.floatlayout import FloatLayout
from kivy.uix.label import Label
from kivy.clock import Clock
from kivy.core.window import Window 

class Page():
    def __init__(self, pos, size):
        self.x, self.y  = pos
        self.size = size

class SwipePlane( ScatterPlane ):
    """ Upgrades: 
    1. Deactivate un-viewed widgets.
    2. Don't press buttons when swiped.
    """
    PAGE_W, PAGE_H = Window.width, Window.height
    def __init__(self, *args, **kwargs):
        """ScatterPlane that supports swipe navigation between pages."""
        super(type(self), self).__init__(*args, **kwargs)
        self.do_rotation, self.do_scaling, self.do_translation_y = False, False, False
        self.pages = []
        #Only allow one animation loop at a time.
        self.animating = False
        self.xvel = 0
        Window.bind(on_motion=lambda window, etype, e: self.on_motion( etype, e ) )
    
    def add_page( self, widget ):
        page = Page( widget.pos, (self.PAGE_W, self.PAGE_H) )
        self.pages += [page]
        self.add_widget( widget )

    def current_page(self): return int(-self.x / self.PAGE_W)

    def on_touch_up(   self, touch ): 
        #Snap back from width boundaries, because kivy will move the screen by dx w/out inertia. 
        if -self.x < self.pages[0].x:
            self.x_shift( -self.pages[0].x )
        elif -self.x > self.pages[-1].x:
            self.x_shift( -self.pages[-1].x )
        super(type(self), self).on_touch_up( touch )

    def on_motion( self, etype, e ):
        if etype == 'begin':
            self.stop_inertia()
            self.start_page_i = self.current_page()
            self.start_page = self.pages[ self.start_page_i ]
        elif etype == 'end':
            if not self.animating and abs(e.dx) > 20 and not self.off_edge(e.dx):
                self.start_inertia( e.dx/abs(e.dx) * 40 )

    def off_edge(self, dx):
        "Return True if moving @dx will take the window into the black."
        return (dx > 0 and self.start_page_i == 0) or \
            (dx < 0 and self.start_page_i == len(self.pages)-1)

    def start_inertia(self, xvel):
        global SWIPING
        SWIPING = True
        self.animating, self.xvel = True, xvel
        Clock.schedule_interval(self.animate, 1/120.0)

    def stop_inertia(self):
        self.animating, self.xvel = False, 0
        Clock.unschedule(self.animate)
    
    def collide_page( self, page ):
        return page != self.start_page and \
                page.x - abs(self.xvel) <= -self.x <= page.x + abs(self.xvel)

    def x_shift(self, x):
        self.apply_transform( 
            self.transform.inverse().translate( x, 0, 0 )
        )

    def animate( self, dt ):
        for p in self.pages:
            if self.collide_page(p):
                self.stop_inertia()
                self.x_shift( -p.x )
                return 
        self.x += self.xvel
        self.x_shift( self.x )

if __name__ == '__main__':
    from kivy.base import runTouchApp
    from kivy.core.window import Window 
    
    swipe_plane = SwipePlane()

    for i in range(5):
        # Give swipe plane a widget whose pos is within the desired page.
        swipe_plane.add_page( Label( text=str(i), pos=(i*Window.width, 0) ) )

    runTouchApp( swipe_plane )
