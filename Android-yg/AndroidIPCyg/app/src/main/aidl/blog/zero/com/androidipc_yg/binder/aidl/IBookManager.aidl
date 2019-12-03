// IBookManager.aidl
package blog.zero.com.androidipc_yg.binder.aidl;

// Declare any non-default types here with import statements
import blog.zero.com.androidipc_yg.binder.aidl.Book;

interface IBookManager {
    void addBook(in Book book);
    List<Book> getBookList();
}
