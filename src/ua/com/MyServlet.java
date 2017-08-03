package ua.com;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet(urlPatterns="/MyServlet", loadOnStartup=0)
public class MyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Map<Integer, String> users = new ConcurrentHashMap<>();
	private AtomicInteger counter;
	
	@Override
	public void init() throws ServletException {
		System.out.println("Initialization of servlet ");
		super.init();
		users.put(0, "Alexandr");
		users.put(1, "Ivan");
		users.put(2, "Vova");
		counter = new AtomicInteger(2);
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		  
		Integer id = Integer.parseInt(request.getParameter("id"));
		
		PrintWriter out = response.getWriter();
		if (users.get(id) == null) {
			String errorExisting = "An attempt of looking for a user, which has not created yet!";
			System.out.println(errorExisting);
			out.println(errorExisting);
		} else {
			out.println("<p>Hello, " + users.get(id) + "! Your id: " + id + "</p>");
			for(Entry entry : users.entrySet()) {
				out.println("<p>id: " + entry.getKey() + " name: " + entry.getValue() + "</p>");
			}
		}
		
		out.println("<p><a href='http://localhost:8080/WebApp/'>Sign up</a></p>");
		out.println("<p><a href='http://localhost:8080/WebApp/showuser.html'>Show users</a></p>");
		out.close();
	}
	
protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=utf-8");
		
		String name = request.getParameter("name");
		String[] jobs = request.getParameterValues("job");
		String gender = request.getParameter("gender");
		String age18 = request.getParameter("age18");
		Integer id = null;
		HttpSession session = request.getSession();
		PrintWriter out = response.getWriter();
		
		System.out.println("Signed up: " + name);
		
		if (name != null && name != "") {
			session.setAttribute("name", name);
		}
		if   (gender == null) {
			gender = "-";
		}
		if (age18 == null) {
			age18 = "No";
		}
		if (!users.containsValue(name)) {
			id = counter.incrementAndGet();
			users.put(id, name);
			
			System.out.println("Amount of jobs: " + jobs.length);
			for(String job : jobs) {
				System.out.print(job + " ");
			}
			
			System.out.println();
			
			System.out.println("Gender: " + gender);
			
			if (gender.equals("Male")) {
				System.out.println("Is he 18?: " + age18);
			}
			else {
				System.out.println("Is she 18?: " + age18);
			}
			
			out.println("<h3>Hello, " + name + "!<h3>");
			out.println("<p>Jobs: " + Arrays.deepToString(jobs) + "</p>");
			out.println("Gender: " + gender);
			out.println( "<p>Is 18: " + age18 + "</p>");
			out.println(session.getAttribute("name"));
			
		} else {
			String errorExisting = "An attempt of creating user, which has already existed!";
			System.out.println(errorExisting);
			out.println(errorExisting);
		}
		
		out.println("<p><a href='http://localhost:8080/WebApp/'>Sign up</a></p>");
		out.println("<p><a href='http://localhost:8080/WebApp/showuser.html'>Show users</a></p>");
		out.close();
}
}
