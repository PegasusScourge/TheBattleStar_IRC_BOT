public final class ChannelBook{
	
	private int maxChannels = 1024;
	protected Channel channels[];
	protected boolean channelUsed[];
	
	ChannelBook(int maxC){
		maxChannels = maxC;
		channels = new Channel[maxChannels];
		channelUsed = new boolean[maxChannels];
		clearChannels();
	}
	
	ChannelBook(){
		channels = new Channel[maxChannels];
		channelUsed = new boolean[maxChannels];
		clearChannels();
	}
	
	public final void clearChannels(){
		for(int i=0; i < maxChannels; i++){
			if(channels[i] != null){
				channels[i].dispose();
			}
			channels[i] = null;
			channelUsed[i] = false;
		}
	}
	
	public final void addChannel(Channel c){
		int ref;
		for(ref = 0; ref < maxChannels; ref++){
			if(!channelUsed[ref])
				break;
		}
		
		//Do a failsafe check
		if(ref == (maxChannels-1)){
			if(channelUsed[ref]){
				//LOG ERROR
				return;
			}
		}
		
		//We got a good handle
		channels[ref] = c;
		channelUsed[ref] = true;
	}
	
	public final void removeChannel(int r){
		if(channelUsed[r]){
			channelUsed[r] = false;
			channels[r].dispose();
			channels[r] = null;
		}
	}
	
	public final void removeChannel(Channel c){
		removeChannel(getChannelRefByObject(c));
	}
	
	public final int getChannelRefByObject(Channel c){
		int ref;
		for(ref = 0; ref < maxChannels; ref++){
			if(channelUsed[ref]){
				if(c.equals(channels[ref])){
					return ref;
				}
			}
		}
		return -1;
	}
	
	public final Channel getChannelObjByRef(int i){
		if(channelUsed[i]){
			return channels[i];
		}
		return null;
	}
	
	public final Channel[] getChannels(){
		return channels;
	}
}