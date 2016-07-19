package comms;

public class RemoteEvent {

	private int encode;
	private String content;
	private static String SEPERATOR = "::";
	
	public RemoteEvent(int _encode, String _content) {
		encode = _encode;
		content = _content;
	}

	public RemoteEvent(String str) throws Exception {
		
		String[] array = str.split(SEPERATOR);
		for (int i = 0; i < array.length; i++) {
			switch (i) {
			case 0:
				encode = new Integer(array[0]).intValue();
				break;
			case 1:
				content = array[1];
				break;
				
			default:
				throw new Exception("Cannot convert from string to object");
			}
		}
	}
	
	public String convertToString() {
		String result = new String();
		result = new Integer(encode).toString() + SEPERATOR + content;
		return result;
	}

	public int getEncode() {
		return encode;
	}

	public void setEncode(int encode) {
		this.encode = encode;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return convertToString();
	}

	
}
