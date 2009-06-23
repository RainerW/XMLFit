package fit;


public class Argument
{

  private Parse cell;
  
  private boolean exception;
  
  private boolean wrong;
  
  private boolean right;
  
  public boolean isRight()
  {
    return right;
  }

  public void setRight(boolean right)
  {
    this.right = right;
  }

  public Argument(Parse cell)
  {
    this.cell = cell;
  }
  
  public boolean isException()
  {
    return exception;
  }

  public void setException(boolean exception)
  {
    this.exception = exception;
  }

  public boolean isWrong()
  {
    return wrong;
  }

  public void setWrong(boolean wrong)
  {
    this.wrong = wrong;
  }

  public Parse getCell()
  {
    return cell;
  }

  public String text()
  {
    return cell.text();
  }
  
}
