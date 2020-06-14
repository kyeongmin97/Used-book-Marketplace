package usedbookmarketplace.model.filesystem;

import java.io.*;
import java.util.Scanner;
import java.util.Vector;

import usedbookmarketplace.model.data.Book;
import usedbookmarketplace.model.data.user.Admin;
import usedbookmarketplace.model.data.user.GeneralUser;
import usedbookmarketplace.model.data.user.User;

public class FileProcess {

	// read a bookDB file
	public Vector<Book> readBookFile(String fileName) {
		Vector<Book> bookList = new Vector<Book>();

		try {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);

			while (scan.hasNext()) {
				String line = scan.nextLine();

				if (!line.startsWith("//") && !line.isEmpty()) {
					String[] tokens = line.split(":");
					
					if (tokens.length != 9)
						throw new Exception("Invalid fileInput");
					bookList.add(new Book(tokens));
				}
			}
			scan.close();
			return bookList;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(0);
		}
		return null;
	}

	// read an accountDB file
	public Vector<User> readAccountFile(String fileName) {
		Vector<User> accountList = new Vector<User>();

		try {
			File file = new File(fileName);
			Scanner scan = new Scanner(file);
			
			while (scan.hasNext()) {
				String line = scan.nextLine();
				
				if (line.startsWith("admin:"))		// 파일 읽을 때 예외처리??
					accountList.add(new Admin(line.split(":")));
				else if (!line.isEmpty())
					accountList.add(new GeneralUser(line.split(":")));
			}
			
			scan.close();
			return accountList;
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// write all accounts or books to the file
	public <T> void writeFile(Vector<T> DB, String fileName) {
		try {
			File file = new File(fileName);
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));
			String[] info;

			if (file.isFile() && file.canWrite()) {
				if (DB.size() != 0) {
					for (int i = 0; i < DB.size() - 1; i++) {
						if (DB.get(0) instanceof Book)					// write 하고자 하는 것이 책인 경우
							info = ((Book) DB.get(i)).getBookInfo();
						else
							info = ((User) DB.get(i)).getUserInfo();
						
						for (int j = 0; j < info.length - 1; j++)		// write 정보.
							bw.write(info[j] + ":");
						bw.write(info[info.length - 1]);
						
						if (i != DB.size())
							bw.newLine();
					}
					
					if (DB.get(0) instanceof Book)
						info = ((Book) DB.get(DB.size() - 1)).getBookInfo();
					else
						info = ((User) DB.get(DB.size() - 1)).getUserInfo();
					
					for (int j = 0; j < info.length - 1; j++)
						bw.write(info[j] + ":");
					bw.write(info[info.length - 1]);
				}
				else
					bw.write("");
				bw.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
