package bean;

import businessLogic.ApplicationFacadeInterfaceWS;
import businessLogic.FacadeImplementationWS;

public class FacadeBean {
	private static FacadeBean singleton = new FacadeBean();
	private static ApplicationFacadeInterfaceWS facadeInterface;

	private FacadeBean() {
		try {
			facadeInterface = new FacadeImplementationWS();
		} catch (Exception e) {
			System.out.println("FacadeBean: negozioaren logika sortzean errorea: " + e.getMessage());
		}
	}

	public static ApplicationFacadeInterfaceWS getBusinessLogic() {
		return facadeInterface;
	}
}