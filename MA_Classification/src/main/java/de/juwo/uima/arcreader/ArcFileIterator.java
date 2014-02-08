package de.juwo.uima.arcreader;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.archive.io.ArchiveReader;
import org.archive.io.ArchiveReaderFactory;
import org.archive.io.ArchiveRecord;

/**
 * Class to iterate over records/files in ARC-File
 * @author Prof. Dr. Herta minor changes by rene helbig
 *
 */
public class ArcFileIterator implements Iterator<ArchiveRecord>{
    
	private ArchiveReader reader;
    private Iterator<ArchiveRecord> arcRecordIterator;
    private ArchiveRecord arcHeaderRecord;
    private ArchiveRecord prefetchedArcRecord;
    private boolean notFetchedYet;

    /**
     * @param arcFile the arc-file to open
     * @throws IOException problems during opening the arc-file
     */
    public ArcFileIterator(File arcFile) throws IOException {
        if (arcFile == null) {
            throw new IllegalArgumentException("ArcFileIterator doesn't support " + 
				arcFile + " as an argument.");
        }
        this.reader = ArchiveReaderFactory.get(arcFile);
        this.arcRecordIterator = this.reader.iterator();
        // skip first record because it contains meta information for the whole arc-file
        if (this.arcRecordIterator.hasNext()) {
            this.arcHeaderRecord = this.arcRecordIterator.next();
        }
    }

    /**
     * @see java.util.Iterator#hasNext()
     */
    public boolean hasNext() {
        boolean result = false;
        // check if there is any prefetched record waiting
        if (this.prefetchedArcRecord != null && this.notFetchedYet) {
            result = true;
        } else if (this.arcRecordIterator != null) {
            // if not, check iterator.hasNext() and prefetch record if there is any
            result = this.arcRecordIterator.hasNext();
            if (result) {
                this.prefetchedArcRecord = this.arcRecordIterator.next();
                this.notFetchedYet = true;
            }
        }
        return result;
    }

    /**
     * calling next() will first check for next records existence
     * @see java.util.Iterator#next()
     */
    public ArchiveRecord next() {
        ArchiveRecord record = null;
        // checking for next record will already get the record into " this.prefetchedArcRecord"
        if (this.hasNext()) {
            record = this.prefetchedArcRecord;
        }
        this.notFetchedYet = false;
        return record;
    }

    /**
     * Important: The ArcFileIterator cannot remove records from ARC-Files!!
     * @throws UnsupportedOperationException when trying to remove a record from an ARC-File
     * @see java.util.Iterator#remove()
     */
    public void remove() {
        throw new UnsupportedOperationException("The ArcFileIterator cannot remove records from ARC-Files");

    }

    /**
     * @return the current header record of the arc-file
     */
    public ArchiveRecord getCurrentArcFileHeader() {
        return this.arcHeaderRecord;
    }
}
