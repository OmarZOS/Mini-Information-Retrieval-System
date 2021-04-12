package Scripts;

import java.util.HashMap;
import java.util.Map;

public class Lookup {
    
    static private Lookup INSTANCE = null;
	
	private Lookup() {}
	
	static public Lookup getLookup () {
		if(INSTANCE==null)
			INSTANCE=new Lookup();
		return INSTANCE ; 
	}
	
	private Map<Class, Object> services = new HashMap<Class, Object>() ;
	
	public <T> void register (
			Class<? super T> service,
			T instance) {
		services.put(service, instance) ;
	}
	
	public <T> T get(Class<T> service) {
		T instance = (T) (services.get(service)) ;
		return instance ; 
	}


}
