package in.bookstore.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import in.bookstore.model.Book;
import in.bookstore.util.ConnectionFactory;

public class BookDAO {

    public boolean insertBook(Book book) {
        String insert = "INSERT INTO books (book_id, title, author, price) VALUES (?, ?, ?, ?)";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(insert)) {

            pstm.setInt(1, book.getId());
            pstm.setString(2, book.getTitle());
            pstm.setString(3, book.getAuthor());
            pstm.setFloat(4, book.getPrice());
            return pstm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateBook(Book book) {
        String update = "UPDATE books SET title=?, author=?, price=? WHERE book_id=?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(update)) {

            pstm.setString(1, book.getTitle());
            pstm.setString(2, book.getAuthor());
            pstm.setFloat(3, book.getPrice());
            pstm.setInt(4, book.getId());
            return pstm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteBook(Book book) {
        String delete = "DELETE FROM books WHERE book_id=?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(delete)) {

            pstm.setInt(1, book.getId());
            return pstm.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Book getBook(int id) {
        Book book = null;
        String select = "SELECT * FROM books WHERE book_id=?";
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement pstm = con.prepareStatement(select)) {

            pstm.setInt(1, id);
            ResultSet rs = pstm.executeQuery();
            if (rs.next()) {
                book = new Book(id, rs.getString("title"), rs.getString("author"), rs.getFloat("price"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return book;
    }

    public List<Book> listAllBook() {
        List<Book> list = new ArrayList<>();
        String sql = "SELECT * FROM books";
        try (Connection con = ConnectionFactory.getConnection();
             Statement stm = con.createStatement();
             ResultSet rs = stm.executeQuery(sql)) {

            while (rs.next()) {
                Book book = new Book(rs.getInt("book_id"), rs.getString("title"),
                        rs.getString("author"), rs.getFloat("price"));
                list.add(book);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
