package in.bookstore.servlet;

import java.io.IOException;
import java.util.List;

import in.bookstore.dao.BookDAO;
import in.bookstore.model.Book;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/book")
public class BookServlet extends HttpServlet {

    private BookDAO bookDAO;

    @Override
    public void init() throws ServletException {
        bookDAO = new BookDAO();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        String idParam = req.getParameter("id");

        if ("delete".equals(action) && idParam != null) {
            int bookId = Integer.parseInt(idParam);
            boolean success = bookDAO.deleteBook(new Book(bookId));
            req.setAttribute("msg", success ? "Book deleted successfully." : "Failed to delete book.");
        } else if ("edit".equals(action) && idParam != null) {
            int bookId = Integer.parseInt(idParam);
            Book book = bookDAO.getBook(bookId);
            req.setAttribute("book", book);
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
            return;
        }

        // Always list all books
        List<Book> list = bookDAO.listAllBook();
        req.setAttribute("listBook", list);
        req.getRequestDispatcher("/viewBook.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(req.getParameter("id"));
            String title = req.getParameter("title");
            String author = req.getParameter("author");
            float price = Float.parseFloat(req.getParameter("price"));

            Book book = new Book(id, title, author, price);

            // Check if book with this ID already exists
            Book existing = bookDAO.getBook(id);
            boolean success;
            String msg;

            if (existing != null) {
                // Update existing book
                success = bookDAO.updateBook(book);
                msg = success ? "Book updated successfully." : "Failed to update book.";
            } else {
                // Insert new book
                success = bookDAO.insertBook(book);
                msg = success ? "Book saved successfully." : "Failed to save book.";
            }

            req.setAttribute("msg", msg);
            req.setAttribute("book", null); // reset form
            req.getRequestDispatcher("/index.jsp").forward(req, resp);

        } catch (NumberFormatException e) {
            req.setAttribute("msg", "Invalid input. Please check ID and price.");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } catch (Exception e) {
            req.setAttribute("msg", "Error: " + e.getMessage());
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        }
    }
}
