package cn.webfuse.framework.web.vo;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 分页结构的返回值
 *
 * @param <T>
 * @author Jesen
 */
public class PageableResponse<T> implements Serializable {

    private int page;
    private int pageSize;
    private long totalElement;
    private List<T> content;
    private Integer totalPage;
    private Integer offset;
    private Boolean isFirst;
    private Boolean isLast;


    public PageableResponse(int page, int pageSize, long totalElement, List<T> content) {
        this.page = page;
        this.pageSize = pageSize;
        this.totalElement = totalElement;
        this.content = content;
    }

    public PageableResponse() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getTotalElement() {
        return totalElement;
    }

    public void setTotalElement(long totalElement) {
        this.totalElement = totalElement;
    }

    public List<T> getContent() {
        return Collections.unmodifiableList(content);
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public int getTotalPage() {
        if (this.totalPage == null) {
            return getPageSize() == 0 ? 1 : (int) Math.ceil((double) totalElement / (double) getPageSize());
        }
        return totalPage;
    }

    public void setTotalPage(int totalPage) {
        this.totalPage = totalPage;
    }

    public int getOffset() {
        if (this.offset == null) {
            return (page - 1) * pageSize;
        }
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isFirst() {
        if (isFirst == null) {
            return !isHasPrevious();
        }
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public boolean isLast() {
        if (isLast == null) {
            return !isHasNext();
        }
        return isLast;
    }

    public void setLast(boolean last) {
        isLast = last;
    }

    private boolean isHasNext() {
        return getPage() + 1 < getTotalPage();
    }


    private boolean isHasPrevious() {
        return getPage() > 1;
    }


}
