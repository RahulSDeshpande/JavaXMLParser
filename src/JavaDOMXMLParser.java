import java.io.*;
import java.util.*;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class JavaDOMXMLParser{
	
	public static JavaDOMXMLParser jdxp;
	
	public NodeList nodeList = null;
	public List<Employee> empList = null;
	
	public String xmlDBFile = null;
	public static boolean xmlDBFileLoaded = false;
	public static int menuChoice;
	public static String searchKey;
	public static String idKey;
	public static boolean recsFound = false;
	
	public int counter = 0;
	
	public String exceptionTypes[] = {"FileNotFoundException", "ParserConfigurationException","XYZException","PQRSException"};
	
	public static void main(String[] args) throws Exception {
	
		jdxp = new JavaDOMXMLParser();
		
		jdxp.menuHandling(); 
	}
	
	public void menuHandling()
	{
		//////////// MENU ////////////
		while(true)
		{
			System.out.println("\n=======");
			System.out.println("DB MENU");
			System.out.println("=======");
			System.out.println("1] Load XML DB File");
			System.out.println("2] View all DB Records");
			System.out.println("3] Search DB Records");
			System.out.println("*] Add DB Records");
			System.out.println("4] Modify DB Records");
			System.out.println("5] Duplicate DB Records");
			System.out.println("6] Delete DB Records");
			System.out.println("*] Alter Table");
			System.out.println("*] Alter Database");
			System.out.println("*] Drop Table");
			System.out.println("*] Drop Database");
			System.out.println("7] Exit =>");
			System.out.print("\nEnter your choice: ");
			Scanner sc = new Scanner(System.in);
			menuChoice = sc.nextInt();
		
			switch(menuChoice)
			{
				case 1:
					jdxp.loadXMLDBFile();
					break;
				case 2:
					jdxp.viewRecords();
					break;
				case 3:
					jdxp.searchRecords();
					break;
				case 4:
					jdxp.modifyRecords();
					break;
				case 5:
					jdxp.duplicateRecords();
					break;
				case 6:
					jdxp.deleteRecords();
					break;
				case 7:
					System.out.println("\n\nThank you.. Exiting.\n");
					System.exit(0);
					break;
				default:
			}
		}
		///////////////////////////////
	}
	
	public void loadXMLDBFile()
	{
		System.out.println("\n================");
		System.out.println("Load XML DB File");
		System.out.println("================");
		System.out.print("Please enter XML DB file path: ");
		Scanner sc = new Scanner(System.in);
		xmlDBFile = sc.nextLine();
		init();
	}
	
	public void init()
	{
		try
		{
			File file = new File(xmlDBFile);
            if (file.exists())
			{
				// Get the DOM Builder Factory
				DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
				
				// Get the DOM Builder
				DocumentBuilder builder = factory.newDocumentBuilder();
				
				// Load and Parse the XML document document contains the complete XML as a Tree
				Document document = builder.parse(xmlDBFile);
	
				empList = new ArrayList<>();
				
				// Iterating through the nodes and extracting the data
				nodeList = document.getDocumentElement().getChildNodes();
				
				System.out.println("\n================================================");
				System.out.println("> '"+xmlDBFile+"' file loaded successfully..");
				System.out.println("================================================");
			}
			else
			{
				System.out.println("\n==============================================");
				System.out.println("> '"+xmlDBFile+"' file NOT FOUND.. Try again..");
				System.out.println("==============================================");
				jdxp.loadXMLDBFile();
			}
		}
		catch (Exception e)
		{
			System.err.println(e);
		}
	}
	
	public void exceptionHandling(int exceptionCode, Exception e)
	{
		switch(exceptionCode)
		{
			case 1:
				System.out.println(e);
				System.out.println("\n'"+xmlDBFile+"' file NOT FOUND.. Try again..\n");
				loadXMLDBFile();
				break;
			case 2:
				System.out.println(e.getMessage());
				System.out.println("\nError occurred.. Try again..\n");
				loadXMLDBFile();
				break;
			case 3:
				break;
			case 4:
				break;
		}
	}
	
	public void viewRecords()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
		
		System.out.println("\n=====================================================");
		System.out.println("ID\tName\tAddress\tContact");
		System.out.println("=====================================================");
		
		for (int i = 0; i < nodeList.getLength(); i++) {
		
			//We have encountered an <record> tag//////////////////////////////////
			Node node = nodeList.item(i);
			if (node instanceof Element) {
				Employee emp = new Employee();
				emp.id = node.getAttributes().getNamedItem("id").getNodeValue();
		
				// System.out.println(emp.id);
				
				//if (idKey.equals(emp.id))	// NitinMuley helped here to replace '==' by 'equals()'
				//{
					//recsFound = true;
					NodeList childNodes = node.getChildNodes();
					for (int j = 0; j < childNodes.getLength(); j++) {
						Node cNode = childNodes.item(j);
	
						// Identifying the child tag of employee encountered.
						if (cNode instanceof Element) {
							String data = cNode.getLastChild().getTextContent().trim();
							switch (cNode.getNodeName()) {
							case "id":
									emp.id = data;
									break;
								case "name":
									emp.name = data;
									break;
								case "address":
									emp.address = data;
									break;
								case "contact":
									emp.contact = data;
									break;
							}
						}
					}
					empList.add(emp);
				//}
				//else
				//{
				//	continue;
				//}
			}

		}

		// Printing the Employee list populated
		for (Employee emp : empList)
		{
			System.out.println(emp);
			////System.out.printf("%-10s %-10s %-10s\n", "osne", "two", "thredsfe");
			////System.out.format("%3d", j) 
		}
		
		if(!recsFound)
		{
			//System.out.println("No records found..");
		}
		System.out.println("=====================================================");
		
	}
	
	public void searchRecords()
	{
		System.out.println("\n===================");
		System.out.println("Search DB Records");
		System.out.println("===================");
		
		System.out.print("Enter ID: ");
		Scanner sc = new Scanner(System.in);
		idKey = sc.nextLine();
		System.out.println("\nidKey: "+idKey+"\n");
	}
	
	public void addRecords()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
	}
	
	public void modifyRecords()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
	}
	
	public void duplicateRecords()
	{
	}
	
	public void deleteRecords()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
	}
	
	public void alterTable()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
	}
	
	public void alterDatabase()
	{
		System.out.println("\n===================");
		System.out.println("View all DB Records");
		System.out.println("===================");
	}
}

 class Employee{
	String id;
	String name;
	String address;
	String contact;

	@Override
	public String toString() {
		// Need to change this by using 'printf()' or 'format()' - RSD
		return id+"\t"+name+"\t"+address+"\t"+contact;
	}
 }
