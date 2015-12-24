Client that sends device motion data over UDP.

This iteration uses CMDeviceMotion.attitude to get corrected device attitude.

Upgrades that would be nice:
	Adjustable input hz of device data.
	Adjustable output hz of output UDP packets.
	Button that caches the current attitude to be used in subsequent calls to multiplyByInverseOfAttitude. This achieves the effect of
	       setting a new 'zero' orientation.
