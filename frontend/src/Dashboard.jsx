import {useState} from "react";
import InputDiv from "./InputDiv.jsx";
import axios from "axios";
export default function Dashboard() {

    const backendUrl = import.meta.env.VITE_BACKEND_URL;
    const [passwordDto, setPasswordDto] = useState({
        siteUsername: "",
        password: "",
        siteUrl: "",
        email: "",
        phoneNumber: "",
        createdAt: "",
        notes: ""
    });

    function handleChange(e) {
        const {name, value} = e.target;
        setPasswordDto({...passwordDto, [name]: value});
    }

    function handleSubmit(e) {
        e.preventDefault();
        console.log("submit");
        axios
            .post(`${backendUrl}/save-password-entry`, passwordDto)
            .then(res => {
                console.log(res.data)
            })
            .catch(err => console.log(err));
    }

    return (
        <>
            <h1>Create Password Entry</h1>
            <div >
                <InputDiv
                    label="Site Username"
                    type="text"
                    name="siteUsername"
                    placeholder="username used on the site"
                    value={passwordDto.siteUsername}
                    onChange={handleChange}
                    required={true}
                />
                <InputDiv
                    label="Password"
                    type="password"
                    name="password"
                    placeholder="password"
                    value={passwordDto.password}
                    onChange={handleChange}
                    required={true}
                />
                <InputDiv
                    label="Site URL"
                    type="url"
                    name="siteUrl"
                    placeholder="https://example.com"
                    value={passwordDto.siteUrl}
                    onChange={handleChange}
                />
                <InputDiv
                    label="Email (Optional)"
                    type="email"
                    name="email"
                    placeholder="user@example.com"
                    value={passwordDto.email}
                    onChange={handleChange}
                />

                <InputDiv
                    label="Phone Number (Optional)"
                    type="tel"
                    name="phoneNumber"
                    placeholder="+91-1234567890"
                    value={passwordDto.phoneNumber}
                    onChange={handleChange}
                />

                <InputDiv
                    label="Created At"
                    type="date"
                    name="createdAt"
                    value={passwordDto.createdAt}
                    onChange={handleChange}
                />
                <InputDiv
                    label="Notes"
                    name="notes"
                    rows="5"
                    cols="40"
                    placeholder="Any extra info"
                    value={passwordDto.notes}
                    onChange={handleChange}
                />
                <div>
                    <button type="submit" onClick={handleSubmit}>Save</button>
                    <button type="reset">Reset</button>
                </div>
            </div>
        </>);
}