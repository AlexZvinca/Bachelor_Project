function TextInput({ label, type, value, onChange, placeholder }) {
    return (
        <div className="inputGroup">
            <label>{label}</label>
            <input
                type={type}
                value={value}
                onChange={onChange}
                placeholder={placeholder}
                className="input"
                required
            />
        </div>
    );
}

export default TextInput;