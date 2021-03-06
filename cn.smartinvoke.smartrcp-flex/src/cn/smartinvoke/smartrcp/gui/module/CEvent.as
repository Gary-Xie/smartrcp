package cn.smartinvoke.smartrcp.gui.module
{
	[Bindable]
	[RemoteClass(alias="cn.smartinvoke.smartrcp.gui.module.CEvent")]
	public class CEvent
	{
		public function CEvent()
		{
		}
/**
	 * the type of event, as defined by the event type constants
	 * in class <code>SWT</code>
	 *
	 * @see org.eclipse.swt.SWT
	 */
	public var type:int;
	
	/**
	 * the event specific detail field, as defined by the detail constants
	 * in class <code>SWT</code>
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public var detail:int;
	/**
	 * the index of the item where the event occurred
	 * 
	 * @since 3.2
	 */
	public var index:int;
	
	
	/**
	 * depending on the event type, the x offset of the bounding
	 * rectangle of the region that requires painting or the
	 * widget-relative, x coordinate of the pointer at the
	 * time the mouse button was pressed or released
	 */
	public var x:int;
	
	/**
	 * depending on the event type, the y offset of the bounding
	 * rectangle of the  region that requires painting or the
	 * widget-relative, y coordinate of the pointer at the
	 * time the mouse button was pressed or released
	 */
	public var y:int;
	
	/**
	 * the width of the bounding rectangle of the 
	 * region that requires painting
	 */
	public var width:int;
	
	/**
	 * the height of the bounding rectangle of the 
	 * region that requires painting
	 */
	public var height:int;

	/**
	 * depending on the event type, the number of following
	 * paint events which are pending which may always be zero
	 * on some platforms or the number of lines or pages to
	 * scroll using the mouse wheel
	 */
	public var count:int;
	
	/**
	 * the time that the event occurred.
	 * 
	 * NOTE: This field is an unsigned integer and should
	 * be AND'ed with 0xFFFFFFFFL so that it can be treated
	 * as a signed long.
	 */	
	public var time:int;
	
	/**
	 * the button that was pressed or released; 1 for the
	 * first button, 2 for the second button, and 3 for the
	 * third button, etc.
	 */	
	public var button:int;
	
	/**
	 * depending on the event, the character represented by the key
	 * that was typed.  This is the final character that results
	 * after all modifiers have been applied.  For example, when the
	 * user types Ctrl+A, the character value is 0x01 (ASCII SOH).
	 * It is important that applications do not attempt to modify the
	 * character value based on a stateMask (such as SWT.CTRL) or the
	 * resulting character will not be correct.
	 */
	public var character:String;
	
	/**
	 * depending on the event, the key code of the key that was typed,
	 * as defined by the key code constants in class <code>SWT</code>.
	 * When the character field of the event is ambiguous, this field
	 * contains the unaffected value of the original character.  For
	 * example, typing Ctrl+M or Enter both result in the character '\r'
	 * but the keyCode field will also contain '\r' when Enter was typed
	 * and 'm' when Ctrl+M was typed.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public var keyCode:int;
	
	/**
	 * depending on the event, the state of the keyboard modifier
	 * keys and mouse masks at the time the event was generated.
	 * 
	 * @see org.eclipse.swt.SWT
	 */
	public var stateMask:int;
	
	/**
	 * depending on the event, the range of text being modified.
	 * Setting these fields has no effect.
	 */
	public var start:int;
	public var end:int;
	
	/**
	 * depending on the event, the new text that will be inserted.
	 * Setting this field will change the text that is about to
	 * be inserted or deleted.
	 */
	public var text:String;
	}
}