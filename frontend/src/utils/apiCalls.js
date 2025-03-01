import urlConfig from "../consts/url-config";


const postData = async (type, data) => {
    try {
      const response = await fetch(urlConfig[type], {
        method: 'POST', // Specifies the request method
        headers: {
          'Content-Type': 'application/json', // Specifies the content type
        },
        body: JSON.stringify(data), // Converts JavaScript object to JSON
      });
  
      if (!response.ok) {
        throw new Error(`HTTP error! Status: ${response.status}`);
      }
    await response.json(); // Parses JSON response
      window.alert('Success:', data);
    } catch (error) {
        console.log(error)
        window.alert('Failed:', data);
    }
  };

export default {
   postData : postData
}