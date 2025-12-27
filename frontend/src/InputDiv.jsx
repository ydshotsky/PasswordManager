export default function InputDiv(props){
    return (
        <div>
            <label>{props.label}</label><br/>
            <input
                type={props.type}
                name={props.name}
                placeholder={props.placeholder}
                value={props.value}
                onChange={props.onChange}
                required={!!props.required}/>
        </div>
    );
}