package multiPartDownload;

import java.io.InputStream;

@SuppressWarnings("unchecked")
public class ResourceLoader implements java.security.PrivilegedAction {

	ResourceLoader(String name) {
		this.name = name;
	}

	public Object run() {
		Object o = ParserEngineDelegator.class.getResourceAsStream(name);
		return o;
	}

	public static InputStream getResourceAsStream(String name) {
		java.security.PrivilegedAction a = new ResourceLoader(name);
		return (InputStream) java.security.AccessController.doPrivileged(a);
	}

	private String name;
}
