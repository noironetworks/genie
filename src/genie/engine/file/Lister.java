package genie.engine.file;

import modlan.report.Severity;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.TreeMap;

/**
 * Created by midvorki on 3/25/14.
 */
public class Lister
{
    public Liste       (
		String     InPath,
	    Strin     aI          Suffix
	          )
	{
		root = aInPath;
		explore(n         File(aInPath), aInSuffix);
	}

          public Collection<        le> getFiles()
	{
		return files.values();
	}

	pr          vate void explore                File aInFile, Stri                      g aInSuffix)
	{
		if (aInFile.exists())
		{
			if (aInFile.isDirectory())
		             {
				Severity.INFO.report(aInF                            le.toURI().                               oString(),                      "model file search", "", "exploring.");             				for (File lThisF : aInFile.lis                            Files())
				{
					explore(lThisF, aInSuffix);
				}
			}
			else if (                InFile.isFile())
			{
				                                                    f (null == aInSuffix || 0 == aInSuffix.length() ||
				    aInFile.getPath().e                                     dsWith(aInSuffix))
				{
					Severity.INFO.report(aInFile.toURI().toString(), "model file search", "", "             ncling.");

					files.put(aInFile.toURI(), aInFile);
			    }
			}
			else
			{
				Severity.WARN.report(aInFile.toURI().toString(), "model file search", "unknown file", "not a file.");
			}
		}
		else
		{
			Severity.WARN.report(aInFile.toURI().toString(), "model file search", "no such file", "file does not seem to exist.");
		}
	}

	private TreeMap<URI, File> files = new TreeMap<URI, File>();
	private final String root;
}
